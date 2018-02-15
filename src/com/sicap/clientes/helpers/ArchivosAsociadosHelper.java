package com.sicap.clientes.helpers;

import java.util.Date;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.DocumentoFormalizDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ArchivoAsociadoVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.DocumentoFormalizVO;
import org.apache.commons.fileupload.FileItem;
import java.io.IOException;
import java.io.File; 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.CopyOption;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Calendar;

public class ArchivosAsociadosHelper {

    public static boolean existe(SolicitudVO solicitud, int tipoArchivo) {
        return existe(solicitud.archivosAsociados, tipoArchivo);
    }

    public static boolean existe(ArchivoAsociadoVO[] archivos, int tipoArchivo) {

        boolean existe = false;
        if (archivos != null && archivos.length > 0) {
            for (int i = 0; i < archivos.length; i++) {
                if (archivos[i].tipo == tipoArchivo) {
                    existe = true;
                }
            }
        }

        return existe;
    }

    public static boolean esFormatoValido(com.jspsmart.upload.File archivo) {
        
        boolean permitido = false;
        if (archivo != null && archivo.getFileExt() != null) {
            if (archivo.getFieldName().equals("amortizacion")) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_AMORTIZACION.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_AMORTIZACION[i].equalsIgnoreCase(archivo.getFileExt())) {
                        permitido = true;
                    }
                }
            } else if (archivo.getFieldName().equals("pagosReferenciadosHSBC")) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF[i].equalsIgnoreCase(archivo.getFileExt())) {
                        permitido = true;
                    }
                }

            } else if (archivo.getFileName().substring(0, 5).equals("HSBC_") || archivo.getFileName().substring(0, 5).equals("BNTE_")
                    || archivo.getFileName().substring(0, 5).equals("BSFI_") || archivo.getFileName().substring(0, 5).equals("BMER_")
                    || archivo.getFileName().substring(0, 5).equals("AFME_") || archivo.getFileName().substring(0, 5).equals("COMA_")
                    || archivo.getFileName().substring(0, 7).equals("BANAMEX") || archivo.getFileName().substring(0, 6).equals("SCOTIA")
                    || archivo.getFileName().substring(0, 5).equals("BAJIO") || archivo.getFileName().substring(0, 6).equals("COMER_")
                    || archivo.getFileName().substring(0, 5).equals("OXXO_") || archivo.getFileName().substring(0, 8).equals("TELECOM_")
                    || archivo.getFileName().substring(0, 6).equals("FBENA_") || archivo.getFileName().substring(0, 5).equals("FABC_")
                    || archivo.getFileName().substring(0, 7).equals("FRESKO_")|| archivo.getFileName().substring(0, 10).equals("SANTANDER_")
                    || archivo.getFileName().substring(0, 7).equals("SORIANA")|| archivo.getFileName().substring(0, 7).equals("CASALEY")
                    || archivo.getFileName().substring(0, 7).equals("OPENPAY")  
                    ) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF[i].equalsIgnoreCase(archivo.getFileExt())) {
                        permitido = true;
                    }
                }

            } else if (archivo.getFileName().substring(0, 4).equals("IBS_")) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF[i].equalsIgnoreCase(archivo.getFileExt())) {
                        permitido = true;
                    }
                }

            } else if (archivo.getFieldName().equals("pagosReferenciadosBanorte")) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF[i].equalsIgnoreCase(archivo.getFileExt())) {
                        permitido = true;
                    }
                }
            } else if (archivo.getFieldName().equals("imagen") || archivo.getFieldName().equals("imagenvivienda") || archivo.getFieldName().equals("imagenvivienda2") || archivo.getFieldName().equals("imagenvivienda3")) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_IMAGEN.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_IMAGEN[i].equalsIgnoreCase(archivo.getFileExt())) {
                        permitido = true;
                    }
                }
            } else if (archivo.getFileName().indexOf("DocumentosGrupales") != -1) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_IMAGEN.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_IMAGEN[i].equalsIgnoreCase(archivo.getFileExt())) {
                        permitido = true;
                    }
                }
            }else if (archivo.getFileName().substring(0, 11).equals("RECHAZADOS_")) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_RECHAZADOS_PRE.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_RECHAZADOS_PRE[i].equalsIgnoreCase(archivo.getFileExt())) {
                        permitido = true;
                        System.out.println("com.sicap.clientes.helpers.ArchivosAsociadosHelper.esFormatoValido()");
                    }
                }

            } else {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_AUTORIZACION.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_AUTORIZACION[i].equalsIgnoreCase(archivo.getFileExt())) {
                        permitido = true;
                    }
                }
            }
        }
        return permitido;
    }


    public static boolean esFormatoValido(FileItem archivo) {
        
        boolean permitido = false;
        String nombre = archivo.getName();
        String extension = getExtension(nombre);
        if ( archivo!=null && extension!=null) {
            if (archivo.getFieldName().equals("amortizacion")) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_AMORTIZACION.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_AMORTIZACION[i].equalsIgnoreCase(extension)) {
                        permitido = true;
                    }
                }
            } else if (archivo.getFieldName().equals("pagosReferenciadosHSBC")) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF[i].equalsIgnoreCase(extension)) {
                        permitido = true;
                    }
                }

            } else if (nombre.substring(0, 5).equals("HSBC_") || nombre.substring(0, 5).equals("BNTE_")
                    || nombre.substring(0, 5).equals("BSFI_") || nombre.substring(0, 5).equals("BMER_")
                    || nombre.substring(0, 5).equals("AFME_") || nombre.substring(0, 5).equals("COMA_")
                    || nombre.substring(0, 7).equals("BANAMEX") || nombre.substring(0, 6).equals("SCOTIA")
                    || nombre.substring(0, 5).equals("BAJIO") || nombre.substring(0, 6).equals("COMER_")
                    || nombre.substring(0, 5).equals("OXXO_") || nombre.substring(0, 8).equals("TELECOM_")) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF[i].equalsIgnoreCase(extension)) {
                        permitido = true;
                    }
                }

            } else if (nombre.substring(0, 4).equals("IBS_")) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF[i].equalsIgnoreCase(extension)) {
                        permitido = true;
                    }
                }

            } else if (archivo.getFieldName().equals("pagosReferenciadosBanorte")) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_PAGOS_REF[i].equalsIgnoreCase(extension)) {
                        permitido = true;
                    }
                }
            } else if (archivo.getFieldName().equals("imagen") || archivo.getFieldName().equals("imagenvivienda") || archivo.getFieldName().equals("imagenvivienda2") || archivo.getFieldName().equals("imagenvivienda3")) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_IMAGEN.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_IMAGEN[i].equalsIgnoreCase(extension)) {
                        permitido = true;
                    }
                }
            } else if (nombre.indexOf("DocumentosGrupales") != -1) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_IMAGEN.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_IMAGEN[i].equalsIgnoreCase(extension)) {
                        permitido = true;
                    }
                }
            } else {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_AUTORIZACION.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_AUTORIZACION[i].equalsIgnoreCase(extension)) {
                        permitido = true;
                    }
                }
            }
        }
        return permitido;
    }

    
    public static String getExtension(String nombre) {

        int indicePunto = nombre.lastIndexOf('.');
        String ext = nombre.substring(indicePunto+1);
        return ext;

    }

    public static int getIndice(ArchivoAsociadoVO[] archivos, int tipoArchivo) {

        int indice = -1;
        if (archivos != null && archivos.length > 0) {
            for (int i = 0; i < archivos.length; i++) {
                if (archivos[i].tipo == tipoArchivo) {
                    indice = i;
                }
            }
        }

        return indice;
    }

    public static int getIndiceGrupal(ArchivoAsociadoVO[] archivos, int identificador) {

        int indice = -1;
        if (archivos != null && archivos.length > 0) {
            for (int i = 0; i < archivos.length; i++) {
                if (archivos[i].consecutivo == identificador) {
                    indice = i;
                }
            }
        }

        return indice;
    }

    public static String getTexto(ArchivoAsociadoVO[] archivos, int tipoArchivo, int tipoOperacion, int idSolicitud) {

        String texto = "Sin archivo";
        if (tipoArchivo == ClientesConstants.ARCHIVO_TIPO_AMORTIZACION) {
            texto = "";
        }
        boolean existe = false;
        if (archivos != null && archivos.length > 0) {
            for (int i = 0; i < archivos.length; i++) {
                if (archivos[i].tipo == tipoArchivo) {
                    existe = true;
                }
            }
        }

        if (tipoOperacion == ClientesConstants.CONSUMO || tipoOperacion == ClientesConstants.VIVIENDA || tipoOperacion == ClientesConstants.CREDIHOGAR || tipoOperacion == ClientesConstants.SELL_FINANCE) {
            existe = true;
        }
        if (existe) {
            if (tipoArchivo == ClientesConstants.ARCHIVO_TIPO_AMORTIZACION) {
                texto = "<a href=\"#\" onClick=\"muestraDocumento('ORDENPAGO')\">Consulta Orden de Pago</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                texto += "<a href=\"#\" onClick=\"muestraDocumento('CONTRATO')\">Consulta contrato</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                texto += "<a href='generaFormatoSaldoDeudor.jsp?idSolicitud=" + idSolicitud + "'>Saldo deudor</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                texto += "<a href=\"#\" onClick=\"muestraDocumento('PAGARE')\">Consulta pagaré</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                if (tipoOperacion != ClientesConstants.SELL_FINANCE) {
                    texto += "<a href='generaFichasPagos.jsp?idSolicitud=" + idSolicitud + "'>Fichas de pago</a><td>";
                }
            } else {
                texto = "<a href=\"#\" onClick=\"muestraArchivo(" + tipoArchivo + ")\">Ver archivo</a>";
            }
        }
        if (tipoOperacion == ClientesConstants.GRUPAL || tipoOperacion == ClientesConstants.REESTRUCTURA_GRUPAL) {
            if (tipoArchivo == ClientesConstants.ARCHIVO_TIPO_AMORTIZACION) {
                texto = "";
                texto += "<a href='generaFormatoSaldoDeudor.jsp?idSolicitud=" + idSolicitud + "'>Saldo deudor</a>";
                texto += "";
            }
        }
        return texto;
    }

    public static String getTextoGrupal(ArchivoAsociadoVO[] archivos, int tipoArchivo, int identificador) {

        String texto = "Sin archivo";
        boolean existe = false;
        if (archivos != null && archivos.length > 0) {
            for (int i = 0; i < archivos.length; i++) {
                if (archivos[i].consecutivo == identificador) {
                    existe = true;
                }
            }
        }

        if (existe) {
            texto = "<a href=\"#\" onClick=\"muestraArchivo(" + identificador + ")\">Ver archivo</a>";
        }
        return texto;
    }
    public static String getBotonCambioEstatus(ArchivoAsociadoVO[] archivos, int identificador, SolicitudVO solicitud){
        String texto = "";
        if (archivos != null && archivos.length > 0) {
            if (solicitud.estatus == ClientesConstants.SOLICITUD_NUEVA ){
            texto = "<td colspan=\"2\" align=\"center\">\n" +
                    " <br><input type=\"button\" id=\"boton\" onclick=\"EnviaraMesa()\" value=\"Enviar a Analisis\">\n" +
                    " </td>";
            }
            if (solicitud.estatus == ClientesConstants.SOLICITUD_PENDIENTE){
                texto = "<td colspan=\"2\" align=\"center\">\n" +
                    " <br><input type=\"button\" id=\"boton\" onclick=\"EnviaraMesa()\" value=\"Enviar a Revaloracion\">\n" +
                    " </td>";
            }
        }
        return texto;
    }

    public static String getTexto(SolicitudVO solicitud) {

        String texto = "";
        boolean tablaCaducada = false;
        int tipoOperacion = solicitud.tipoOperacion;
        if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO || solicitud.solicitudReestructura != 0) {

            if (tipoOperacion != ClientesConstants.GRUPAL && tipoOperacion != ClientesConstants.REESTRUCTURA_GRUPAL) {
                if (solicitud.amortizacion != null && solicitud.amortizacion.length > 0) {
                    int opcion = muestraOpcionesIndividual(solicitud);
                    System.out.println("opcion" + opcion);
                    if (solicitud.reestructura != 0 || opcion == 1 || opcion == 2) {
                        if (solicitud.tipoOperacion == ClientesConstants.VIVIENDA) {
                            texto = "<a href=\"#\" onClick=\"muestraDocumento('VIVIENDA')\">Consulta contrato</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                        } else if (solicitud.tipoOperacion == ClientesConstants.SELL_FINANCE) {
                            texto += "<a href='muestraContratoPDF.jsp?idSolicitud=" + solicitud.idSolicitud + "'>Consulta Contrato</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                            texto += "<a href='muestraSolicitudDescuentoPDF.jsp?idSolicitud=" + solicitud.idSolicitud + "'>ConsultaSolicitudDescuento</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                            texto += "<a href='muestraSolicitudPDF.jsp?idSolicitud=" + solicitud.idSolicitud + "'>ConsultaSolicitud</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                        } else {
                            /*DOCUENTOS MICROCREDITO*/
                            texto += "<a href='muestraContratoPDF.jsp?idSolicitud=" + solicitud.idSolicitud + "'>Consulta Contrato</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                            texto += "<a href='muestraAutorizacionDesembolsoPDF.jsp?idSolicitud=" + solicitud.idSolicitud + "'>Autorizacion Desembolso</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                        }

                        if (opcion == 2) {
                            texto += "<a href=\"#\" onClick=\"muestraDocumento('ORDENPAGO')\">Consulta Orden de Pago</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                        }
                        texto += "<a href='generaFormatoSaldoDeudor.jsp?idSolicitud=" + solicitud.idSolicitud + "'>Saldo deudor</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                        if(solicitud.tipoOperacion == ClientesConstants.MICROCREDITO)
                            texto += "<a href='generaPagareMicroPDF.jsp?idSolicitud=" + solicitud.idSolicitud + "'>Consulta pagaré</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                        else
                            texto += "<a href=\"#\" onClick=\"muestraDocumento('PAGARE')\">Consulta pagaré</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                        texto += "<a href='generaFichasPagos.jsp?idSolicitud=" + solicitud.idSolicitud + "'>Fichas de pago</a>";

                    }
                    if (opcion == 3) {
                        tablaCaducada = true;
                        texto += "<font color='red'>La tabla de autorización tiene más de tres días de antiguedad</font>";
                    }
                }
            }
            if (tipoOperacion == ClientesConstants.GRUPAL || tipoOperacion == ClientesConstants.REESTRUCTURA_GRUPAL) {
                //texto +=  "<td colspan='2' align='center'>";
                texto += "<a href='generaFormatoSaldoDeudor.jsp?idSolicitud=" + solicitud.idSolicitud + "'>Saldo deudor</a>";
            }
        }
        if (solicitud.decisionComite != null && !tablaCaducada) {
            if (tipoOperacion == ClientesConstants.CREDIHOGAR || tipoOperacion == ClientesConstants.CONSUMO) {
                texto += "&nbsp;&nbsp;&nbsp;&nbsp;";
                texto += "<a href='generaHojaRespuesta.jsp?idSolicitud=" + solicitud.idSolicitud + "'>Hoja de Respuesta</a>";
            }
        }
        return texto;
    }

    public static int muestraOpcionesIndividual(SolicitudVO solicitud) {
        int value = 0;
        boolean isOrdenDePago = false;
        int diferencia = FechasUtil.inBetweenDays(solicitud.amortizacion[0].fechaPago, new Date());
// En caso de Descuento Nomina la diferencia no se calcula		

        if (solicitud.ordenPago != null && solicitud.ordenPago.getReferencia() != null) {
            isOrdenDePago = true;
        }
        if (isOrdenDePago) {
            if (solicitud.ordenPago.getEstatus() == ClientesConstants.OP_DISPERSADA || solicitud.ordenPago.getEstatus() == ClientesConstants.OP_COBRADA || solicitud.ordenPago.getEstatus() == ClientesConstants.OP_DESEMBOLSADO) {
                value = 2;
            } else {
                value = 0;
            }
        } else {
            if (solicitud.desembolsado == ClientesConstants.DESEMBOLSADO || diferencia < 3) {
                value = 1;
            } else {
                value = 3;
            }

        }
        return value;
    }

    public static String getRutaArchivo(int idCliente, int idSolicitud) {
        return getRutaArchivo(idCliente, idSolicitud, "");
    }

    public static String getRutaArchivo(int idCliente, int idSolicitud, String tipoCliente) {

        String ruta = null;
        ruta = "C:\\Users\\flopez\\Documents\\ArchivosPruebas";
//        ruta = ClientesConstants.RUTA_BASE_ARCHIVOS + tipoCliente + idCliente + "\\" + idSolicitud + "\\";
        return ruta;
    }

    public static String getNombreArchivo(ClienteVO cliente, int idSolicitud, int tipoArchivo) throws ClientesException {

        String nombreArchivo = null;
        ArchivoAsociadoVO[] archivos;
        if (tipoArchivo == ClientesConstants.ARCHIVO_TIPO_REPORTE_VISITA_GRUPAL) {
            archivos = new ArchivoAsociadoDAO().getArchivos(cliente.idCliente, idSolicitud);
        } else {
            int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
            SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
            archivos = solicitud.archivosAsociados;
        }
        if (archivos != null && archivos.length > 0) {
            for (int i = 0; i < archivos.length; i++) {
                if (archivos[i].tipo == tipoArchivo) {
                    nombreArchivo = archivos[i].nombre;
                }
            }
        }

        return nombreArchivo;
    }

    public static String getNombreArchivoGrupal(CicloGrupalVO ciclo, int identificador) throws ClientesException {

        String nombreArchivo = null;
        ArchivoAsociadoVO[] archivos;
        archivos = ciclo.archivosAsociados;

        if (archivos != null && archivos.length > 0) {
            for (int i = 0; i < archivos.length; i++) {
                if (archivos[i].consecutivo == identificador) {
                    nombreArchivo = archivos[i].nombre;
                }
            }
        }

        return nombreArchivo;
    }
    
    public static String copiaArchivosHeredados(ClienteVO cliente, int idSolicitud, int tipoArchivo) throws ClientesException {

        String nombreArchivo = null;
        ArchivoAsociadoVO[] archivos;
        if (tipoArchivo == ClientesConstants.ARCHIVO_TIPO_REPORTE_VISITA_GRUPAL) {
            archivos = new ArchivoAsociadoDAO().getArchivos(cliente.idCliente, idSolicitud);
        } else {
            int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
            SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
            archivos = solicitud.archivosAsociados;
        }
        if (archivos != null && archivos.length > 0) {
            for (int i = 0; i < archivos.length; i++) {
                if (archivos[i].tipo == tipoArchivo) {
                    nombreArchivo = archivos[i].nombre;
                }
            }
        }

        return nombreArchivo;
    }
    public static void copyFile_Java7(String origen, String destino) throws IOException {
        Path FROM = Paths.get(origen);
        Path TO = Paths.get(destino);
        //sobreescribir el fichero de destino, si existe, y copiar
        // los atributos, incluyendo los permisos rwx
        CopyOption[] options = new CopyOption[]{
          StandardCopyOption.REPLACE_EXISTING,
          StandardCopyOption.COPY_ATTRIBUTES
        }; 
        Files.copy(FROM, TO, options);
    }
    
    public static boolean EsPosibleHeredarArchivos(ClienteVO cliente, SolicitudVO solicitudActual, SolicitudVO solicitudAnterior, int indiceSolicitud, int cantidadSolicitudes) throws IOException 
    {
        Date fechaUltimoCambioEnDocumentacion = SolicitudUtil.DeterminaFechaUltimoCambioEnDocumentacion(cliente);
        
        if(solicitudActual.documentacionCompleta == 0)
        {
           if(solicitudActual.estatus == 0 || solicitudActual.estatus == ClientesConstants.SOLICITUD_CAPTURADO || solicitudActual.estatus == ClientesConstants.SOLICITUD_PREAPROBADA)
           {
                if(fechaUltimoCambioEnDocumentacion != null)
                {
                    int diasAcomparar = 365;
                    Calendar fechaAComparar = Calendar.getInstance();
                    fechaAComparar.setTime(fechaUltimoCambioEnDocumentacion);
                    if(FechasUtil.fechaDentroDelRango(fechaAComparar, diasAcomparar))
                    {
                        if(indiceSolicitud  == (cantidadSolicitudes - 1))
                        {
                            if(cantidadSolicitudes > 1)
                            { 
                                return true;
                            }  
                        }

                    }
                }
            }
        }
       return false;
    }
 
}

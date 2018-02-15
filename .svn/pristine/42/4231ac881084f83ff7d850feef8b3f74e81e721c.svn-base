package com.sicap.clientes.helpers;

import com.jspsmart.upload.Request;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.IncidenciasMonitorPagosDAO;
import com.sicap.clientes.dao.PagoGrupalDAO;
import com.sicap.clientes.dao.PagoIndividualGruposDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ComisionVO;
import com.sicap.clientes.vo.EventosDePagoVO;
import com.sicap.clientes.vo.IncidenciasMonitorPagosVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.PagoGrupalVO;
import com.sicap.clientes.vo.PagoIndividualGruposVO;
import com.sicap.clientes.vo.ReporteVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import com.sicap.clientes.vo.TasaInteresVO;
import java.text.DecimalFormatSymbols;
import org.apache.log4j.Logger;

public class HTMLHelper {

    private static Logger myLogger = Logger.getLogger(HTMLHelper.class);

    public static String muestraTasa(int idTasa, TreeMap<Integer, TasaInteresVO> tasas) {
        String tasaStr = "";
        double tasaIva = 0;
        if (idTasa != 0) {
            TasaInteresVO tasa = tasas.get(new Integer(idTasa));
            if (tasa != null) {
                tasaIva = (tasa.valor * 1.15) / 12;
                tasaStr = formatoTasaComision(tasaIva);
            }
        }
        return tasaStr;
    }

    public static String muestraComision(int idComision, TreeMap<Integer, ComisionVO> comisiones) {
        String comisionStr = "";
        double tasaIva = 0;
        if (idComision != 0) {
            ComisionVO comision = comisiones.get(new Integer(idComision));
            if (comision != null) {
                tasaIva = comision.porcentaje;
                comisionStr = formatoTasaComision(tasaIva);
            }
        }
        return comisionStr;
    }

    public static String formatoTasaComision(double field) {

        DecimalFormat myFormatter = new DecimalFormat("###.0");
        String output = "";

        if (field != 0) {
            output = myFormatter.format(field);
        } else {
            output = "";
        }

        return output;

    }

    public static String displayNotifications(Notification[] notificaciones) {

        String notificacion = "";
        Notification not = null;

        for (int c = 0; notificaciones != null && c < notificaciones.length; c++) {
            not = (Notification) notificaciones[c];
            if(not != null){
                if (not.type == ClientesConstants.ERROR_TYPE) {
                    notificacion += "<b><font color='" + ClientesConstants.ERROR_COLOR + "'>" + not.text + "</font><b><br><br>";
                } else {
                    notificacion += "<b><font color='" + ClientesConstants.INFO_COLOR + "'>" + not.text + "</font></b><br><br>";
                }
            }
        }
        return notificacion;

    }

    public static String displayNotifications(Vector notificaciones) {

        String notificacion = "";
        Notification not = null;

        for (int c = 0; notificaciones != null && c < notificaciones.size(); c++) {
            not = (Notification) notificaciones.get(c);
            if (not.type == ClientesConstants.ERROR_TYPE) {
                notificacion += "<b><font color='" + ClientesConstants.ERROR_COLOR + "'>" + not.text + "</font></b><br><br>";
            } else {
                notificacion += "<b><font color='" + ClientesConstants.INFO_COLOR + "'>" + not.text + "</font></b><br><br>";
            }
        }
        return notificacion;

    }

    public static String displayField(String field) {

        if (field == null) {
            return "";
        } else {
            return field;
        }

    }

    public static String displayField(String field, String defaultValue) {
        if (field == null || field.equalsIgnoreCase("")) {
            return defaultValue;
        }
        return field;
    }

    public static String displayField(int field) {

        return displayField(field, false);

    }

    public static String displayField(int field, boolean muestraCero) {

        if (field == 0) {
            if (muestraCero) {
                return String.valueOf(field);
            } else {
                return "";
            }
        } else {
            return String.valueOf(field);
        }

    }

    public static String displayField(double field) {

        return displayField(field, false);

    }

    public static String displayField(double field, boolean muestraCero) {

        if (field == 0) {
            if (muestraCero) {
                return String.valueOf(field);
            } else {
                return "";
            }
        } else {
            return String.valueOf(field);
        }

    }

    public static String formatoMonto(double field, double valorDefecto) {

        DecimalFormat myFormatter = new DecimalFormat(ClientesConstants.FORMATO_MONTO);
        String output = "";

        if (field != 0) {
            output = myFormatter.format(field);
        } else {
            output = myFormatter.format(valorDefecto);
        }

        return output;

    }

    public static String formatoMonto(double field) {

        DecimalFormat myFormatter = new DecimalFormat(ClientesConstants.FORMATO_MONTO);
        String output = "";

        if (field != 0) {
            output = myFormatter.format(field);
        } else {
            output = "0.00";
        }

        return output;

    }
    
    public static String formatoMontoMillones(double field) {

        DecimalFormat myFormatter = new DecimalFormat(ClientesConstants.FORMATO_MONTO_MILLONES);
        String output = "";

        if (field != 0) {
            output = myFormatter.format(field);
        } else {
            output = "0.00";
        }

        return output;

    }

    public static String formatoMontoRefinanceado(CicloGrupalVO ciclo) {

        double total = ciclo.saldo.getCapitalVencido() + ciclo.saldo.getSaldoInteresVencido() + ciclo.saldo.getSaldoMora() + ciclo.saldo.getSaldoIVAMora() + ciclo.saldo.getSaldoMulta() + ciclo.saldo.getSaldoIVAMulta();
        DecimalFormat myFormatter = new DecimalFormat(ClientesConstants.FORMATO_MONTO);
        String output = "";
        output = myFormatter.format(total);
        return output;

    }

    /*	public static String formatoMonto(double field){
     return formatoMonto(field, 0);
     }*/
    public static double formatoMontoDouble(double field) {

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat myFormatter = new DecimalFormat(ClientesConstants.FORMATO_MONTO, simbolos);
        String output = "";

        if (field != 0) {
            output = myFormatter.format(field);
        }

        double out = Double.parseDouble(output);
        return out;

    }

    public static double redondeaMonto(double field) {

        double output = 0.00;
        if (field != 0) {
            output = FormatUtil.redondeaMoneda(field);
        }

        return output;

    }

    public static String formatCantidad(double field) {

        NumberFormat myFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        String output = "";
        output = myFormatter.format(field);
        return output;

    }

    public static String formatCantidad(double field, boolean eliminaSigno) {

        String output = formatCantidad(field);
        if (eliminaSigno) {
            output = output.substring(1);
        }

        return output;

    }

//	public static String formatCantidad(double field){
//
//		NumberFormat nf1 = NumberFormat.getInstance(Locale.US); 
//		String output = "";
//
//		if ( field!=0 ){
//			output = nf1.format(field);
//		}else
//			output = "0.00";
//
//		if(output.indexOf('.')==-1)
//			output = output+".00";
//		
//		return output;
//
//	}
    public static String displayField(Date field) {

        return displayField(field, 1);

    }

    public static String displayField(Date field, int tipo) {

        SimpleDateFormat formato = null;
        if (tipo == 2) {
            formato = new SimpleDateFormat(ClientesConstants.FORMATO_HORA);
        } else {
            formato = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA);
        }

        if (field == null) {
            return "";
        } else {
            return formato.format(field);
        }

    }

    public static String displayField(Date field, Date valorDefoult) {

        SimpleDateFormat formato = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA);

        if (field == null) {
            return formato.format(valorDefoult);
        } else {
            return formato.format(field);
        }

    }

    public static String displayField(Timestamp field, String defaultValue) {

        SimpleDateFormat formato = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_HORA);

        if (field == null) {
            return defaultValue;
        } else {
            return formato.format(field);
        }

    }

    public static String displayField(Timestamp field) {

        SimpleDateFormat formato = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_HORA);

        if (field == null) {
            return "";
        } else {
            return formato.format(field);
        }

    }

    public static String displayCombo(TreeMap catalogo, int actual) {
        StringBuffer codigo = new StringBuffer();

        if (catalogo != null && catalogo.size() > 0) {
            Set set = catalogo.keySet();
            Iterator llaves = set.iterator();
            Object key = null;
            while (llaves.hasNext()) {
                key = llaves.next();
                codigo.append("<option ");
                codigo.append("value=\"" + key.toString() + "\" ");
                if (key.toString().equals(String.valueOf(actual))) {
                    codigo.append(" selected ");
                }
                codigo.append(">" + catalogo.get(key).toString());
                codigo.append("</option>\n");
            }
        }
        return codigo.toString();
    }

    public static String displayCombo(TreeMap catalogo, String actual) {

        StringBuffer codigo = new StringBuffer();

        if (catalogo != null && catalogo.size() > 0) {
            Set set = catalogo.keySet();
            Iterator llaves = set.iterator();
            Object key = null;
            while (llaves.hasNext()) {
                key = llaves.next();
                codigo.append("<option ");
                if (key.toString().equals(actual)) {
                    codigo.append("selected ");
                }
                codigo.append("value=\"" + key.toString() + "\">");
                codigo.append(catalogo.get(key).toString());
                codigo.append("</option>");
            }
        }
        return codigo.toString();
    }

    public static String displayComboCheck(TreeMap catalogo, int actual) {

        String codigo = new String();

        if (catalogo != null && catalogo.size() > 0) {
            Set set = catalogo.keySet();
            Iterator llaves = set.iterator();
            Object key = null;
            while (llaves.hasNext()) {
                key = llaves.next();
                codigo += "<option ";
                if (key.toString().equals(String.valueOf(actual))) {
                    codigo += "selected ";
                }
                codigo += "value=\"" + catalogo.get(key).toString() + "\">";
                codigo += catalogo.get(key).toString();
                codigo += "</option>";
            }
        }
        return codigo;
    }

    public static String displayComboAlfabetico(TreeMap catalogo, int actual) {

        String codigo = new String();

        if (catalogo != null && catalogo.size() > 0) {
            Set set = catalogo.keySet();
            Iterator llaves = set.iterator();
            Object key = null;
            while (llaves.hasNext()) {
                key = llaves.next();
                if (catalogo.get(key).toString().equals("0")) {
                    codigo += "<option ";
                    if (catalogo.get(key).toString().equals(String.valueOf(actual))) {
                        codigo += "selected ";
                    }
                    codigo += "value=\"" + catalogo.get(key).toString() + "\">";
                    codigo += key.toString();
                    codigo += "</option>";
                }
            }
            llaves = set.iterator();
            while (llaves.hasNext()) {
                key = llaves.next();
                if (!catalogo.get(key).toString().equals("0")) {
                    codigo += "<option ";
                    if (catalogo.get(key).toString().equals(String.valueOf(actual))) {
                        codigo += "selected ";
                    }
                    codigo += "value=\"" + catalogo.get(key).toString() + "\">";
                    codigo += key.toString();
                    codigo += "</option>";
                }
            }
        }
        return codigo;
    }

    public static String displayCombo(TreeMap catalogo, int actual, int defecto) {

        if (actual != 0) {
            return displayCombo(catalogo, actual);
        } else {
            String codigo = new String();
            if (catalogo != null && catalogo.size() > 0) {
                Set set = catalogo.keySet();
                Iterator llaves = set.iterator();
                Object key = null;
                while (llaves.hasNext()) {
                    key = llaves.next();
                    codigo += "<option ";
                    if (key.toString().equals(String.valueOf(defecto))) {
                        codigo += "selected ";
                    }
                    codigo += "value=\"" + key.toString() + "\">";
                    codigo += catalogo.get(key).toString();
                    codigo += "</option>";
                }
            }
            return codigo;
        }
    }

    public static String displayComboComisiones(TreeMap catalogo, int actual) {

        String codigo = new String();

        if (catalogo != null && catalogo.size() > 0) {
            Set set = catalogo.keySet();
            Iterator llaves = set.iterator();
            Object key = null;
            while (llaves.hasNext()) {
                key = llaves.next();
                ComisionVO elemento = (ComisionVO) catalogo.get(key);
                if (actual == 0) {
                    if (elemento.status.equals("A")) {
                        codigo += "<option ";
                        if (key.toString().equals(String.valueOf(actual))) {
                            codigo += "selected ";
                        }
                        codigo += "value=\"" + key.toString() + "\">";
                        codigo += elemento.descripcion;
                        codigo += "</option>";
                    }
                } else {
                    if (actual == elemento.id || elemento.status.equals("A")) {
                        codigo += "<option ";
                        if (key.toString().equals(String.valueOf(actual))) {
                            codigo += "selected ";
                        }
                        codigo += "value=\"" + key.toString() + "\">";
                        codigo += elemento.descripcion;
                        codigo += "</option>";
                    }
                }
            }
        }
        return codigo;
    }

    public static String displayComboTasas(TreeMap catalogo, int actual) {

        String codigo = new String();

        if (catalogo != null && catalogo.size() > 0) {
            Set set = catalogo.keySet();
            Iterator llaves = set.iterator();
            Object key = null;
            while (llaves.hasNext()) {
                key = llaves.next();
                TasaInteresVO elemento = (TasaInteresVO) catalogo.get(key);
                if (actual == 0) {
                    if (elemento.status.equals("A")) {
                        codigo += "<option ";
                        if (key.toString().equals(String.valueOf(actual))) {
                            codigo += "selected ";
                        }
                        codigo += "value=\"" + key.toString() + "\">";
                        codigo += elemento.descripcion;
                        codigo += "</option>";
                    }
                } else {
                    if (actual == elemento.id || elemento.status.equals("A")) {
                        codigo += "<option ";
                        if (key.toString().equals(String.valueOf(actual))) {
                            codigo += "selected ";
                        }
                        codigo += "value=\"" + key.toString() + "\">";
                        codigo += elemento.descripcion;
                        codigo += "</option>";
                    }
                }
            }
        }
        return codigo;
    }

    public static String displayComboProductosSellFinance(TreeMap catalogo, int actual) {

        String codigo = new String();

        if (catalogo != null && catalogo.size() > 0) {
            Set set = catalogo.keySet();
            Iterator llaves = set.iterator();
            Object key = null;
            while (llaves.hasNext()) {
                key = llaves.next();
                TasaInteresVO elemento = (TasaInteresVO) catalogo.get(key);
                if (actual == 0) {
                    if (elemento.status.equals("A")) {
                        codigo += "<option ";
                        if (key.toString().equals(String.valueOf(actual))) {
                            codigo += "selected ";
                        }
                        codigo += "value=\"" + key.toString() + "\">";
                        codigo += elemento.descripcion;
                        codigo += "</option>";
                    }
                } else {
                    if (actual == elemento.id || elemento.status.equals("A")) {
                        codigo += "<option ";
                        if (key.toString().equals(String.valueOf(actual))) {
                            codigo += "selected ";
                        }
                        codigo += "value=\"" + key.toString() + "\">";
                        codigo += elemento.descripcion;
                        codigo += "</option>";
                    }
                }
            }
        }
        return codigo;
    }

    public static String getDescripcion(TreeMap catalogo, int indice) {

        String descripcion = new String();
        if (indice == 0) {
            return "N/D";
        }
        if (catalogo != null && catalogo.size() > 0) {
            Set set = catalogo.keySet();
            Iterator llaves = set.iterator();
            Object key = null;
            while (llaves.hasNext()) {
                key = llaves.next();
                if (key.toString().equals(String.valueOf(indice))) {
                    descripcion = catalogo.get(key).toString();
                }
            }
        }
        return descripcion;

    }

    public static String getStatusEjecutivo(String status) {
        String regresastatus = "Baja";
        if (status != null && status.equals("A")) {
            regresastatus = "Alta";
        }
        return regresastatus;
    }
    
    public static String getStatusCobrador(int status) {
        String regresastatus = "Inactivo";
        if (status != 0 && status ==1) {
            regresastatus = "Activo";
        }
        return regresastatus;
    }

    public static String getStatusSupervisor(String status) {
        String regresastatus = "Baja";
        if (status != null && status.equals("A")) {
            regresastatus = "Alta";
        }
        return regresastatus;
    }

    public static String getIdAltaStatusEjecutivo(String status) {
        String regresastatus = "B";
        if (status != null && status.equals("Alta")) {
            regresastatus = "A";
        }
        return regresastatus;
    }

    public static String getIdAltaStatusSupervisor(String status) {
        String regresastatus = "B";
        if (status != null && status.equals("Alta")) {
            regresastatus = "A";
        }
        return regresastatus;
    }

    public static String getComboStatusEjecutivo(String status) {
        String codigo = new String();

        String statuscombo = "Baja";
        String statuscomboopuesto = "Alta";
        codigo += "<option";
        if (status != null && status.equals("A")) {
            statuscombo = "Alta";
            statuscomboopuesto = "Baja";
        }
        codigo += " selected value=\"" + statuscombo + "\">" + statuscombo;
        codigo += " <option value=\"" + statuscomboopuesto + "\">" + statuscomboopuesto;

        return codigo;
    }
    public static String getComboFilStatusEjec() {
        String codigo = new String();
        
        String defaultcombo= "NSeleccione...";
        String statuscombo = "BBaja";
        String statuscomboopuesto = "AAlta";
        
        codigo += "<option";        
        codigo += " selected value=\"" + defaultcombo.substring(0,1) + "\">" + defaultcombo.substring(1);
        codigo += " <option value=\"" + statuscombo.substring(0,1) + "\">" + statuscombo.substring(1);
        codigo += " <option value=\"" + statuscomboopuesto.substring(0,1) + "\">" + statuscomboopuesto.substring(1);
        

        return codigo;
    }
    
    public static String getComboStatusCobrador(int status) {
        String codigo = new String();

        String statuscombo = "2Inactivo";
        String statuscomboopuesto = "1Activo";
        codigo += "<option";
        if (status != 0 && status==1) {
            statuscombo = "1Activo";
            statuscomboopuesto = "2Inactivo";
        }
        codigo += " selected value=\"" + statuscombo.substring(0,1) + "\">" + statuscombo.substring(1);
        codigo += " <option value=\"" + statuscomboopuesto.substring(0,1) + "\">" + statuscomboopuesto.substring(1);

        return codigo;
    }

    public static String getComboStatusSupervisor(String status) {
        String codigo = new String();

        String statuscombo = "Baja";
        String statuscomboopuesto = "Alta";
        codigo += "<option";
        if (status != null && status.equals("A")) {
            statuscombo = "Alta";
            statuscomboopuesto = "Baja";
        }
        codigo += " selected value=\"" + statuscombo + "\">" + statuscombo;
        codigo += " <option value=\"" + statuscomboopuesto + "\">" + statuscomboopuesto;

        return codigo;
    }

    public static int getParameterInt(HttpServletRequest req, String nombre) throws Exception {
        int valor = 0;
        if (req.getParameter(nombre) != null && !req.getParameter(nombre).equals("")) {
            valor = Convertidor.stringToInt(req.getParameter(nombre));
        }
        return valor;
    }
    
    /**
     * Metodo que usa el Request de la API de com.​jspsmart.​upload
     * @param req
     * @param nombre
     * @return
     * @throws Exception 
     */
    public static int getParameterInt(Request req, String nombre) throws Exception {
        int valor = 0;
        if (req.getParameter(nombre) != null && !req.getParameter(nombre).equals("")) {
            valor = Convertidor.stringToInt(req.getParameter(nombre));
        }
        return valor;
    }

    public static String getParameternotNull(String nombre) throws Exception {
        String valor = "si";
        if (nombre == null) {
            valor = "no";
        }
        return valor;
    }

    public static float getParameterFloat(HttpServletRequest req, String nombre) throws Exception {
        float valor = 0;
        if (req.getParameter(nombre) != null && !req.getParameter(nombre).equals("")) {

            if (nombre.indexOf(',') > -1) {
                nombre = nombre.replaceAll(",", "");
            }
            valor = Convertidor.stringToFloat(req.getParameter(nombre));
        }
        return valor;
    }
    public static String getColorCalificacion (int calificacion, int aceptaRegular, int idSucursal)throws Exception{
        String color = "#ffffff";
        if (calificacion==ClientesConstants.CALIFICACION_CIRCULO_BUENA)
            color = "#00E100";
        if (calificacion== ClientesConstants.CALIFICACION_CIRCULO_REGULAR){
            if (aceptaRegular>0 &&CatalogoHelper.esSucursalAceptaRegular(idSucursal))
            color = "#ffff00";
            else 
                color ="BDBDBA";
        }
        if (calificacion==  ClientesConstants.CALIFICACION_CIRCULO_MALA)
            color = "#FF0000";
        if (calificacion == ClientesConstants.CALIFICACION_CIRCULO_NA)
            color = "#33FF99";
        if (calificacion == ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA)
            color = "#FFEBCD";
        return color;
    }
    public static double getParameterDouble(HttpServletRequest req, String nombre) throws Exception {
        double valor = 0;
        if (req.getParameter(nombre) != null && !req.getParameter(nombre).equals("")) {
            valor = Convertidor.stringToDouble(req.getParameter(nombre));
        }
        return valor;
    }

    public static String getParameterString(HttpServletRequest req, String nombre) {
        String valor = "";
        if (req.getParameter(nombre) != null) {
            valor = req.getParameter(nombre);
        }
        return valor;
    }

    public static boolean getParameterBoolean(HttpServletRequest req, String nombre) throws Exception {
        int valor = 0;
        boolean resultado = false;
        if (req.getParameter(nombre) != null && !req.getParameter(nombre).equals("")) {
            valor = Convertidor.stringToInt(req.getParameter(nombre));
        }
        resultado = (valor == 1 ? true : false);
        return resultado;
    }

    public static Date getParameterDate(HttpServletRequest req, String nombre) throws Exception {
        Date valor = null;
        if (req.getParameter(nombre) != null && !req.getParameter(nombre).equals("")) {
            valor = Convertidor.stringToDate(req.getParameter(nombre));
        }
        return valor;
    }

    public static java.sql.Date getParameterSqlDate(HttpServletRequest req, String nombre) throws Exception {
        java.sql.Date valor = null;
        SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
        if (req.getParameter(nombre) != null && !req.getParameter(nombre).equals("")) {
            valor = new java.sql.Date(sdf.parse(req.getParameter(nombre)).getTime());
        }
        return valor;
    }

    public static String getFechaHoy() {
        Date hoy = new Date();
        SimpleDateFormat formato = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA);
        return formato.format(hoy);
    }

    /**
     * Muestra combo de sucursales
     *
     * @author L.I. Juan Carlos Garibaldi Rivas
     * @return
     * @throws SQLException
     * @throws LotesException
     */
    public static String displayComboSucursales() throws SQLException, ClientesException {

        StringBuffer codigo = new StringBuffer();
        TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);

        Iterator numSucursales = catSucursales.keySet().iterator();
        while (numSucursales.hasNext()) {
            Integer sucursal = (Integer) numSucursales.next();
            codigo.append("<option ");
            codigo.append("value=\"" + sucursal + "\">");
            codigo.append(catSucursales.get(sucursal));
            codigo.append("</option>");
        }
        return codigo.toString();

    }

    public static String displayCheck(String nombre, boolean seleccionado) {

        String salida = "";
        if (nombre != null && !nombre.equals("")) {
            if (seleccionado) {
                salida = "<input type=\"checkbox\" name=\"" + nombre + "\" id=\"" + nombre + "\" value=\"si\" checked>";
            } else {
                salida = "<input type=\"checkbox\" name=\"" + nombre + "\" id=\"" + nombre + "\" value=\"si\">";
            }
        }
        return salida;
    }
    
    public static String displayCheckEvent(String nombre, boolean seleccionado, String event) {

        String salida = "";
        if (nombre != null && !nombre.equals("")) {
            if (seleccionado) {
                salida = "<input type=\"checkbox\" name=\"" + nombre + "\" id=\"" + nombre + "\" value=\"si\" onchange=\""+event+"\" checked>";
            } else {
                salida = "<input type=\"checkbox\" name=\"" + nombre + "\" id=\"" + nombre + "\" value=\"si\" onchange=\""+event+"\" >";
            }
        }
        return salida;
    }
    
     public static String printRol(int idRol) {

        String salida = "&nbsp;";
        if (idRol != 0) {
            switch (idRol) {
                case 1:
                    salida = "Tesorero";
                    break;
                case 2:
                    salida = "Secretario";
                    break;
                case 3:
                    salida = "Presidente";
                    break;
            }
        }
        return salida;
    }

    public static boolean buscaCliente(String integrantesVisitados, IntegranteCicloVO integarnte) {

        boolean salida = false;
        if (integrantesVisitados != null && integarnte != null) {
            String idCliente = String.valueOf(integarnte.idCliente);
            StringTokenizer tokens = new StringTokenizer(integrantesVisitados);
            while (tokens.hasMoreTokens()) {
                if (idCliente.equals(tokens.nextToken())) {
                    return true;
                }
            }
        }
        return salida;
    }

    public static String opcionesRegistroAlertaCC(CicloGrupalVO ciclo, int idCliente, int idAlerta, int operacion) {

        String salida = "&nbsp;";
        operacion = 2;
        if (idAlerta != -1 && ciclo.eventosDePago[idAlerta].reporteCobranza != null) {

            for (int i = 0; i < ciclo.eventosDePago[idAlerta].reporteCobranza.length; i++) {

                if (ciclo.eventosDePago[idAlerta].reporteCobranza[i].numCliente == idCliente) {
                    operacion = 1;
                    return salida = "<a href=\"#\" onClick=\"consultaInformeCobranza(" + idCliente + "," + idAlerta + "," + operacion + ")\">Consultar</a>";
                }
                for (int a = 0; ciclo.integrantes != null && a < ciclo.integrantes.length; a++) {
                    if (ciclo.integrantes[a].idCliente == idCliente && ciclo.integrantes[a].rol == 0 && (ciclo.eventosDePago[idAlerta].estatusReporteCobranza == 1 || ciclo.eventosDePago[idAlerta].estatusReporteCobranza == 2)) {
                        salida = "<a href=\"#\" onClick=\"consultaInformeCobranza(" + idCliente + "," + idAlerta + "," + operacion + ")\">Agregar</a>";
                    }
                }
            }
        } else {
            for (int a = 0; ciclo.integrantes != null && a < ciclo.integrantes.length; a++) {
                if (ciclo.integrantes[a].idCliente == idCliente && ciclo.integrantes[a].rol == 0 && (ciclo.eventosDePago[idAlerta].estatusReporteCobranza == 1 || ciclo.eventosDePago[idAlerta].estatusReporteCobranza == 2)) {
                    salida = "<a href=\"#\" onClick=\"consultaInformeCobranza(" + idCliente + "," + idAlerta + "," + operacion + ")\">Agregar</a>";
                }
            }
        }
        return salida;
    }

    public static String opcionesInformeVisitaGrupal(CicloGrupalVO ciclo, int idAlerta, int operacion) {

        EventosDePagoVO eventoDePago = ciclo.eventosDePago[idAlerta];
        String salida = "&nbsp;";
        operacion = 2;
        if (eventoDePago != null) {
            if (eventoDePago.reporteVisita != null && eventoDePago.reporteVisita.idAlerta == eventoDePago.identificador) {
                operacion = 1;
                return salida = "<a href=\"#\" onClick=\"consultaInformeVisita(" + eventoDePago.identificador + "," + idAlerta + "," + operacion + ")\">Consultar</a>";
            } else if ((eventoDePago.estatusVisitaGerente == 1 || eventoDePago.estatusVisitaSupervisor == 1)) {
                return salida = "<a href=\"#\" onClick=\"consultaInformeVisita(" + eventoDePago.identificador + "," + idAlerta + "," + operacion + ")\">Agregar</a>";
            }
        }

        if (salida.equals("&nbsp;") && ciclo.archivosAsociados != null) {
            for (int t = 0; t < ciclo.archivosAsociados.length; t++) {
                if (ciclo.archivosAsociados[t].consecutivo == eventoDePago.identificador) {
                    return salida = "<a href=\"#\" onClick=\"consultaInformeVisita(" + eventoDePago.identificador + "," + idAlerta + "," + operacion + ")\">Consultar</a>";
                }
            }
        }
        return salida;
    }

    public static String opcionesInformeCobranza(EventosDePagoVO eventoDePago, int idAlerta) {

        String salida = "&nbsp;";
        if (eventoDePago != null) {

            if (eventoDePago.reporteCobranza != null && eventoDePago.reporteCobranza.length >= 2) {
                return salida = "<a href=\"#\" onClick=\"consultaInformeCobranzaGrupal(" + eventoDePago.identificador + "," + idAlerta + ")\">Consultar</a>";
            } else if (eventoDePago.estatusReporteCobranza == 1) {
                return salida = "<a href=\"#\" onClick=\"consultaInformeCobranzaGrupal(" + eventoDePago.identificador + "," + idAlerta + ")\">Agregar</a>";
            }
        }

        return salida;
    }

    public static boolean esUsuarioDF(SucursalVO[] sucursales) {

        for (int i = 0; sucursales != null && i < sucursales.length; i++) {
            if (sucursales[i].idSucursal == 33) {
                return true;
            }
        }
        return false;
    }

    public static String obtieneEstatusAlerta(EventosDePagoVO eventoDePago) throws ClientesException {

        String respuesta = "";
        IncidenciasMonitorPagosVO[] incidencias = new IncidenciasMonitorPagosDAO().getIncidenciasPorId(eventoDePago.identificador);
        for (int i = 0; incidencias != null && i < incidencias.length; i++) {
            if (incidencias[i].visitaSupervisor == 1) {
                respuesta += "Cobrador:Vencida " + incidencias[i].fechaAlerta + " / " + incidencias[i].fechaRegistrada + "\n";
            }
            if (incidencias[i].visitaGerente == 1) {
                respuesta += "Gerente:Vencida " + incidencias[i].fechaAlerta + " / " + incidencias[i].fechaRegistrada + "\n";
            }
            if (incidencias[i].visitaGestor == 1) {
                respuesta += "Gestor:Vencida " + incidencias[i].fechaAlerta + " / " + incidencias[i].fechaRegistrada + "\n";
            }
        }

        if (eventoDePago.estatusVisitaSupervisor == 1) {
            respuesta += "Supervisor: Vigente";
        } else if (eventoDePago.estatusVisitaSupervisor == 2) {
            respuesta += "Supervisor: Atendida";
        }

        if (eventoDePago.estatusVisitaGerente == 1) {
            respuesta += "Gerente: Vigente";
        } else if (eventoDePago.estatusVisitaGerente == 2) {
            respuesta += "Gerente: Atendida";
        }

        if (eventoDePago.estatusVisitaGestor == 1) {
            respuesta += "Gestor: Vigente";
        } else if (eventoDePago.estatusVisitaGestor == 2) {
            respuesta += "Gestor: Atendida";
        }

        if (respuesta.equals("")) {
            respuesta = "No aplica";
        }

        return respuesta;
    }

    public static String displayCheckonClick(String nombre, boolean seleccionado, boolean habilitado, String funcion) {

        String salida = "";
//		String aux  = ( habilitado ? "enabled" : "disabled");
        String aux = (seleccionado ? "disabled" : "enabled");

        if (nombre != null && !nombre.equals("")) {
            if (seleccionado) {
                salida = "<input type=\"checkbox\" name=\"" + nombre + "\" id=\"" + nombre + "\" value=\"si\" onClick=\"" + funcion + "\"" + aux + " checked>";
            } else {
                salida = "<input type=\"checkbox\" name=\"" + nombre + "\" id=\"" + nombre + "\" value=\"si\" onClick=\"" + funcion + "\"" + aux + " >";
            }
        }
        return salida;
    }

    public static String displayCheck(String nombre, boolean seleccionado, boolean deshabilitar) {

        String salida = "";
        String estatus = "";
        if (nombre != null && !nombre.equals("")) {
            if (deshabilitar == true) {
                estatus = " readonly=\"readonly\" disabled ";
            }
            if (seleccionado) {
                salida = "<input type=\"checkbox\" name=\"" + nombre + "\" value=\"si\" checked " + estatus + " >";
            } else {
                salida = "<input type=\"checkbox\" name=\"" + nombre + "\" value=\"si\" " + estatus + " >";
            }
        }
        return salida;
    }
    
    /**
     * JECB 01/10/2017
     * Método de utileria sobrecargado que genera elementos de formulario HTML del tipo
     * checkbox
     * @param nombre nombre que tendra el elemento html en pantalla
     * @param id identificador del elemento html en pantalla
     * @param seleccionado boolean que indica si el control checkbox aparecere seleccionado o no
     * @param deshabilitar boolean que indica si el componente html aparecera habilitado o inhabilitado
     * @return String con segmento html que se embebera en la pantalla
     */
    public static String displayCheck(String nombre, String id, boolean seleccionado, boolean deshabilitar) {

        String salida = "";
        String estatus = "";
        if (nombre != null && !nombre.equals("")) {
            if (deshabilitar == true) {
                estatus = " readonly=\"readonly\" disabled ";
            }
            if (seleccionado) {
                salida = "<input type=\"checkbox\" name=\"" + nombre + "\" id=\"" + id + "\" value=\"si\" checked " + estatus + " >";
            } else {
                salida = "<input type=\"checkbox\" name=\"" + nombre + "\" id=\"" + id + "\" value=\"si\" " + estatus + " >";
            }
        }
        return salida;
    }
    
    public static String displayCheckBool(String nombre, boolean seleccionado, boolean deshabilitar) {

        String salida = "";
        String estatus = "";
        if (nombre != null && !nombre.equals("")) {
            if (deshabilitar == true) {
                estatus = " readonly=\"readonly\" disabled ";
            }
            if (seleccionado) {
                salida = "<input type=\"checkbox\" name=\"" + nombre + "\" value=\"1\" checked " + estatus + " >";
            } else {
                salida = "<input type=\"checkbox\" name=\"" + nombre + "\" value=\"1\" " + estatus + " >";
            }
        }
        return salida;
    }

    public static boolean getCheckBox(HttpServletRequest req, String nombre) throws Exception {

        boolean resultado = false;
        System.out.println("nombre "+req.getParameter(nombre));
        if (req.getParameter(nombre) != null) {
            resultado = true;
        }
        return resultado;
    }

    public static String identificadorUsuario(String identificador) {
        String resultado = "";
        if (identificador.equals("nuevo")) {
            resultado = "Interno:<input type=\"radio\" checked=\"true\" value=\"I\" name=\"identificador\">&nbsp;Externo:<input type=\"radio\" value=\"E\" name=\"identificador\">";
        } else {
            if (!identificador.equals("")) {
                if (identificador.equals("I")) {
                    resultado = "Interno";
                }
                if (identificador.equals("E")) {
                    resultado = "Externo";
                }
            } else {
                resultado = "No identificado";
            }
        }
        return resultado;
    }

    public static String radioBoton(String nombre, boolean dato, boolean soloLectura) {
        String resultado = "";
        if (soloLectura) {
            resultado = "Si:<input type=\"radio\" " + (dato ? "checked=\"checked\"" : "") + " value=\"1\" disabled=\"disabled\" name=\"" + nombre + "\">&nbsp;No:<input type=\"radio\" value=\"0\" " + (!dato ? "checked=\"checked\"" : "") + " disabled=\"disabled\" name=\"" + nombre + "\">";
        } else {
            resultado = "Si:<input type=\"radio\" " + (dato ? "checked=\"checked\"" : "") + " value=\"1\" name=\"" + nombre + "\">&nbsp;No:<input type=\"radio\" value=\"0\" " + (!dato ? "checked=\"checked\"" : "") + " name=\"" + nombre + "\">";
        }
        return resultado;
    }

    public static String estatusSucursal(int identificador) {
        String resultado = "";
        if (identificador == 0) {
            resultado = "Abierta:<input type=\"radio\" value=\"1\" name=\"estatus\">&nbsp;Cerrada:<input type=\"radio\" checked=\"true\" value=\"0\" name=\"estatus\">";
        } else {
            resultado = "Abierta:<input type=\"radio\" checked=\"true\" value=\"1\" name=\"estatus\">&nbsp;Cerrada:<input type=\"radio\" value=\"0\" name=\"estatus\">";
        }
        return resultado;
    }

    public static String getHoraActual() {
        Date hoy = new Date();
        SimpleDateFormat formato = new SimpleDateFormat(ClientesConstants.FORMATO_HORA);
        return formato.format(hoy);
    }

    public static ArrayList<PagoGrupalVO> distribuyePagosPorFecha(CicloGrupalVO ciclo, ArrayList<PagoGrupalVO> pagos) throws ClientesException {

        ArrayList<PagoGrupalVO> todosLosPagos = null;
        todosLosPagos = new PagoGrupalDAO().getPagosGrupales(ciclo.idGrupo, ciclo.idCiclo);
        int plazo = ciclo.plazo;

        for (int a = 0; a < ciclo.tablaAmortizacion.length; a++) {
            GregorianCalendar limiteInferior = new GregorianCalendar();
            GregorianCalendar limiteSuperior = new GregorianCalendar();

            limiteSuperior.setTime(ciclo.tablaAmortizacion[a].fechaPago);
            limiteSuperior.set(Calendar.HOUR_OF_DAY, 0);
            limiteSuperior.set(Calendar.MINUTE, 0);
            limiteSuperior.set(Calendar.SECOND, 0);
            limiteSuperior.set(Calendar.MILLISECOND, 0);
            limiteSuperior.add(Calendar.DAY_OF_YEAR, 4);
            limiteInferior.setTime(ciclo.tablaAmortizacion[a].fechaPago);
            limiteInferior.set(Calendar.DAY_OF_YEAR, limiteInferior.get(Calendar.DAY_OF_YEAR) - 2);
            limiteInferior.set(Calendar.HOUR_OF_DAY, 0);
            limiteInferior.set(Calendar.MINUTE, 0);
            limiteInferior.set(Calendar.SECOND, 0);
            limiteInferior.set(Calendar.MILLISECOND, 0);

            //System.out.println("limiteSuperior     :::   "+limiteSuperior.getTime()+"  :::   ");
            //System.out.println("FECHA VENCIMIENTO  :::   "+ciclo.tablaAmortizacion[a].fechaPago+"  :::   ");
            //System.out.println("limiteInferior     :::   "+limiteInferior.getTime()+"  :::   ");
            PagoGrupalVO pagoTemporal = null;
            for (int i = 0; i < todosLosPagos.size(); i++) {

                GregorianCalendar fechaPago = new GregorianCalendar();
                fechaPago.setTime(todosLosPagos.get(i).fechaPago);
                fechaPago.set(Calendar.HOUR_OF_DAY, 0);
                fechaPago.set(Calendar.MINUTE, 0);
                fechaPago.set(Calendar.SECOND, 0);
                fechaPago.set(Calendar.MILLISECOND, 0);
                //SI EL PAGO TRAE NUMERO DE AMORTIZACION POR DEFECTO ENTRA AQUI Y NO COMPARA RANGO DE FECHAS
                if (todosLosPagos.get(i).numAmortizacion == ciclo.tablaAmortizacion[a].numPago) {
                    if (pagoTemporal == null) {
                        pagoTemporal = new PagoGrupalVO();
                    }
                    pagoTemporal.numGrupo = ciclo.idGrupo;
                    pagoTemporal.numCiclo = ciclo.idCiclo;
                    pagoTemporal.fechaPago = todosLosPagos.get(i).fechaPago;
                    pagoTemporal.numPago = todosLosPagos.get(i).numPago;
                    pagoTemporal.monto += todosLosPagos.get(i).monto;
                    pagoTemporal.multa += todosLosPagos.get(i).multa;
                    pagoTemporal.ahorro += todosLosPagos.get(i).ahorro;
                    pagoTemporal.solidario += todosLosPagos.get(i).solidario;
                    pagoTemporal.numTransaccion = pagoTemporal.numTransaccion + 1;
                    pagoTemporal.numAmortizacion = ciclo.tablaAmortizacion[a].numPago;
                    todosLosPagos.remove(i);
                    i--;
                } //SI NO TRAE NUM PAGO ENTONCES VERIFICA EN QUE NUMERO DE PAGO LO AGREGE
                else if (((fechaPago.getTime().before(limiteSuperior.getTime()) || fechaPago.getTime().equals(limiteSuperior.getTime()))
                        && (fechaPago.getTime().after(limiteInferior.getTime()) || fechaPago.getTime().equals(limiteInferior.getTime())) && todosLosPagos.get(i).numAmortizacion == 0)
                        || ciclo.tablaAmortizacion[a].numPago == plazo) {

                    PagoIndividualGruposDAO pagoIndv = new PagoIndividualGruposDAO();
                    if (pagoIndv.existePagoIndividual(ciclo.tablaAmortizacion[a].idCliente, ciclo.tablaAmortizacion[a].idSolicitud, ciclo.tablaAmortizacion[a].numPago)) {
                        if (ciclo.tablaAmortizacion[a].numPago < plazo) {
                            todosLosPagos.get(i).numAmortizacion = new PagoIndividualGruposDAO().pagoMaximoRegistrado(ciclo.tablaAmortizacion[a].idCliente, ciclo.tablaAmortizacion[a].idSolicitud) + 1;
                            new PagoGrupalDAO().updatePagoGrupal(todosLosPagos.get(i));
                        }
                    } else {
                        if (pagoTemporal == null) {
                            pagoTemporal = new PagoGrupalVO();
                        }
                        pagoTemporal.numGrupo = ciclo.idGrupo;
                        pagoTemporal.numCiclo = ciclo.idCiclo;
                        pagoTemporal.fechaPago = todosLosPagos.get(i).fechaPago;
                        pagoTemporal.numPago = todosLosPagos.get(i).numPago;
                        pagoTemporal.monto += todosLosPagos.get(i).monto;
                        pagoTemporal.multa += todosLosPagos.get(i).multa;
                        pagoTemporal.ahorro += todosLosPagos.get(i).ahorro;
                        pagoTemporal.solidario += todosLosPagos.get(i).solidario;
                        pagoTemporal.numTransaccion = pagoTemporal.numTransaccion + 1;
                        //pagoTemporal.numAmortizacion = ciclo.tablaAmortizacion[a].numPago;

                        if (ciclo.tablaAmortizacion[a].numPago < plazo) {
                            pagoTemporal.numAmortizacion = ciclo.tablaAmortizacion[a].numPago;
                        } else if (ciclo.tablaAmortizacion[a].numPago == plazo) {
                            pagoTemporal.numAmortizacion = plazo;
                        }

                        todosLosPagos.get(i).numAmortizacion = ciclo.tablaAmortizacion[a].numPago;
                        new PagoGrupalDAO().updatePagoGrupal(todosLosPagos.get(i));
                        todosLosPagos.remove(i);
                        i--;
                    }

                }
            }

            if (pagoTemporal != null) {
                pagos.add(pagoTemporal);
            } else {
                pagoTemporal = new PagoGrupalVO();
                pagoTemporal.numGrupo = ciclo.idGrupo;
                pagoTemporal.numCiclo = ciclo.idCiclo;
                pagoTemporal.numPago = ciclo.tablaAmortizacion[a].numPago;
                pagos.add(pagoTemporal);
            }
        }

        return pagos;
    }

    public static String displayMatrizPagoComunalDetalle(int idOperacion, CicloGrupalVO ciclo) throws ClientesException {

        StringBuffer row = new StringBuffer();

        //PagoGrupalDAO pagoGrupaldao=new PagoGrupalDAO();			
        ArrayList<PagoGrupalVO> pagos = new ArrayList<PagoGrupalVO>();
        PagoIndividualGruposDAO pagoIndividualdao = new PagoIndividualGruposDAO();
        TablaAmortizacionVO[] t = ciclo.tablaAmortizacion;
        PagoIndividualGruposVO pagoIndividual = null;
        TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(idOperacion);

        int plazo = ciclo.plazo;
        double[] montoPagoRegistrado = null;
        boolean[] corroborado = null;
        double[] sumaPagosIndividuales = null;  //El la suma total de los pagos individuales
        double[] diferencia = null;
        double[] aPagarPorPersona = null;
        double[] pagoGarantiaPorPersona = null;
        int[] numeroDePagos = null;

        try {
            //obtiene los pagos grupales.  Se utiliza para formar la matriz dependiendo del n�mero de pagos	
            pagos = distribuyePagosPorFecha(ciclo, pagos);

            //Calculos especificos para determinar el pago individual de cada integrante de grupo				
            double montoTotalSemanal = t[1].montoPagar;	//se obtiene de la tabla de amortizacion
            double saldoInicial = t[0].saldoInicial;
            double pagoGarantia = (t[0].saldoInicial - t[0].montoPagar) * ClientesConstants.FRACCION_PAGO_GARANTIA;
            double sumaMontosInd = 0;
            double totalAhorrado = 0;
            if (ciclo.integrantes.length > 0) {
                aPagarPorPersona = new double[ciclo.integrantes.length];
                pagoGarantiaPorPersona = new double[ciclo.integrantes.length];
                //a pagar por cada integrante
                for (int i = 0; ciclo.integrantes != null && i < ciclo.integrantes.length; i++) {
                    double primaConComision = ClientesUtil.calculaMontoConComision(ciclo.integrantes[i].primaSeguro, ciclo.integrantes[i].comision, catComisiones);
                    aPagarPorPersona[i] = FormatUtil.redondeaMoneda(((ciclo.integrantes[i].monto + primaConComision) / saldoInicial) * montoTotalSemanal);
                    aPagarPorPersona[i] = Math.ceil(aPagarPorPersona[i]);
                    pagoGarantiaPorPersona[i] = FormatUtil.redondeaMoneda(((ciclo.integrantes[i].monto + primaConComision) / saldoInicial) * pagoGarantia);
                    sumaMontosInd += aPagarPorPersona[i];
                }
            }//fin montos sugeridos

            montoPagoRegistrado = new double[pagos.size()];
            corroborado = new boolean[pagos.size()];
            sumaPagosIndividuales = new double[pagos.size()];
            diferencia = new double[pagos.size()];
            numeroDePagos = new int[pagos.size()];

            for (int i = 0; i < pagos.size(); i++) {
                sumaPagosIndividuales[i] = FormatUtil.roundDouble(pagoIndividualdao.sumaPagosIndividuales(ciclo.idGrupo, ciclo.idCiclo, pagos.get(i).numAmortizacion), 2);
                montoPagoRegistrado[i] = pagos.get(i).monto;
                diferencia[i] = montoPagoRegistrado[i] - montoTotalSemanal;
                numeroDePagos[i] = pagos.get(i).numTransaccion;
                totalAhorrado += pagos.get(i).ahorro;
            }
            //Fin totales
            //Forma la Matriz de Pagos Comunales
            for (int i = 0; ciclo.integrantes != null && i < ciclo.integrantes.length; i++) {
                //obtiene el n�mero de cliente y lo muestra
                row.append("<tr><td> " + ciclo.integrantes[i].idCliente + "</td>");
                //nombre del cliente
                row.append("<td> " + ciclo.integrantes[i].nombre.trim() + "</td>");
                row.append("<td align='right'> " + formatCantidad(aPagarPorPersona[i]) + "</td>");
                int y = 0;
                //if(pagos.size()>0) {
                //forma los pagos individuales
                for (y = 0; y < pagos.size(); y++) {
                    //obtiene el pago por cada cliente
                    // Unicamente busca el pago si el monto es mayor a 0.	
                    if (pagos.get(y).monto == 0) {
                        pagoIndividual = null;
                    } else {
                        pagoIndividual = pagoIndividualdao.getPagoIndividualGrupos(ciclo.idGrupo, ciclo.idCiclo, ciclo.integrantes[i].idCliente, pagos.get(y).numAmortizacion);
                    }
                    if (pagoIndividual == null) {
                        //CUADRO DE TEXTO PARA CAPTURAR PAGO
                        if (pagos.get(y).monto == 0) {
                            row.append("<td align='right'><input type=\"text\" name=\"pago" + (y + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"pago" + (y + 0) + "\"  value=" + 0 + " readonly class=\"soloLectura\" /></td>\n");
                        } else // En caso de ser el pago 0 se sugiere el pago en garantia
                        if (y == 0) {
                            row.append("<td align='right'><input type=\"text\" name=\"pago" + (y + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"pago" + (y + 0) + "\" value=" + HTMLHelper.formatoMonto(pagoGarantiaPorPersona[i]) + "  /></td>\n");
                        } else {
                            row.append("<td align='right'><input type=\"text\" name=\"pago" + (y + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"pago" + (y + 0) + "\" value=" + HTMLHelper.formatoMonto(aPagarPorPersona[i]) + "  /></td>\n");
                        }
                    } else {
                        if (pagoIndividual.corroborar == 1) {
                            row.append("<td align='right'><input type=\"text\" name=\"pago" + (y + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"pago" + (y + 0) + "\"  value=" + HTMLHelper.formatoMonto(pagoIndividual.monto) + " readonly class=\"soloLectura\" /></td>\n");
                            corroborado[y] = true;
                        } else {
                            row.append("<td align='right'><input type=\"text\" name=\"pago" + (y + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"pago" + (y + 0) + "\"  value=" + HTMLHelper.formatoMonto(pagoIndividual.monto, aPagarPorPersona[i]) + " /></td>\n");
                        }
                    }
                }  //fin for
                //completa los pagos faltantes con ceros
                for (int j = y; j < plazo; j++) {
                    row.append("<td align='right'><input type=\"text\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"pago" + (j + 1) + "\"  value=" + 0 + " readOnly class=\"soloLectura\" /></td>\n");
                }
                row.append("</tr>\n");
            }	//fin for integrantes
            // AGREGAMOS AL SOLIDARIO			
            row.append("<tr>\n<td style=\"font-weight: bold\" colspan='2' align='right'>SOLIDARIO</td>\n"
                    + "<td style=\"font-size: small\">&nbsp</td>\n");
            int sol = 0;
            for (sol = 0; sol < pagos.size(); sol++) {
                if (pagos == null) {
                    row.append("<td align='right'><input type=\"text\" name=\"solidario" + (sol + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"solidario" + (sol + 0) + "\" value=\"0\" /></td>\n");
                } else {
                    if (pagoIndividualdao.existePagoIndividual(ciclo.idGrupo, ciclo.idCiclo, pagos.get(sol).numAmortizacion)) {
                        row.append("<td align='right'><input type=\"text\" name=\"solidario" + (sol + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"solidario" + (sol + 0) + "\"  value=" + HTMLHelper.formatoMonto(pagos.get(sol).solidario) + " readonly class=\"soloLectura\" /></td>\n");
                    } else {
                        if (pagos.get(sol).monto == 0) {
                            row.append("<td align='right'><input type=\"text\" name=\"solidario" + (sol + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"solidario" + (sol + 0) + "\" value=\"0\" readonly class=\"soloLectura\" /></td>\n");
                        } else {
                            row.append("<td align='right'><input type=\"text\" name=\"solidario" + (sol + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"solidario" + (sol + 0) + "\"  value=" + HTMLHelper.formatoMonto(pagos.get(sol).solidario) + " /></td>\n");
                        }
                    }
                }

            }
            //	completa los pagos faltantes con ceros
            for (int j = sol; j < plazo; j++) {
                row.append("<td><input type=\"text\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"solidario" + (j + 1) + "\"  value=" + 0 + " readOnly class=\"soloLectura\" /></td>\n");
            }
            row.append("</tr>\n");
            // AGREGAMOS AHORRO			
            row.append("<tr>\n<td style=\"font-weight: bold\" colspan='2' align='right'>GARANT&Iacute;A</td>\n"
                    + "<td bgcolor=\"red\">" + formatCantidad(totalAhorrado) + "</td>\n");

            int ahorro = 0;
            for (ahorro = 0; ahorro < pagos.size(); ahorro++) {
                if (pagos == null) {

                    row.append("<td><input type=\"text\" name=\"ahorro" + (ahorro + 1) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"ahorro" + (ahorro + 1) + "\" value=0  /></td>\n");
                } else {
                    if (pagoIndividualdao.existePagoIndividual(ciclo.idGrupo, ciclo.idCiclo, pagos.get(ahorro).numAmortizacion)) {
                        row.append("<td><input type=\"text\" name=\"ahorro" + (ahorro + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"ahorro" + (ahorro + 0) + "\"  value=" + HTMLHelper.formatoMonto(pagos.get(ahorro).ahorro) + " readonly class=\"soloLectura\" /></td>\n");
                    } else {
                        if (pagos.get(ahorro).monto == 0) {
                            row.append("<td><input type=\"text\" name=\"ahorro" + (ahorro + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"ahorro" + (ahorro + 0) + "\"  value=\"0\" readonly class=\"soloLectura\" /></td>\n");
                        } else {
                            row.append("<td><input type=\"text\" name=\"ahorro" + (ahorro + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"ahorro" + (ahorro + 0) + "\"  value=" + HTMLHelper.formatoMonto(pagos.get(ahorro).ahorro) + " /></td>\n");
                        }
                    }
                }
            }

            //	completa los pagos faltantes con ceros
            for (int j = ahorro; j < plazo; j++) {
                row.append("<td><input type=\"text\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"ahorro" + (j + 1) + "\"  value=" + 0 + " readOnly class=\"soloLectura\" /></td>\n");
            }
            row.append("</tr>\n");

            //	AGREGAMOS MULTAS			
            row.append("<tr>\n<td style=\"font-weight: bold\" colspan='2' align='right'>MULTA</td>\n"
                    + "<td width=\"20px\" style=\"font-size: small\">&nbsp</td>\n");

            int multa = 0;
            for (multa = 0; multa < pagos.size(); multa++) {
                if (pagos == null) {
                    row.append("<td><input type=\"text\" name=\"multa" + (multa + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"multa" + (multa + 0) + "\" value=0  /></td>\n");
                } else {
                    if (pagoIndividualdao.existePagoIndividual(ciclo.idGrupo, ciclo.idCiclo, pagos.get(multa).numAmortizacion)) {
                        row.append("<td><input type=\"text\" name=\"multa" + (multa + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"multa" + (multa + 0) + "\"  value=" + HTMLHelper.formatoMonto(pagos.get(multa).multa) + " readonly class=\"soloLectura\" /></td>\n");
                    } else {
                        if (pagos.get(multa).monto == 0) {
                            row.append("<td><input type=\"text\" name=\"multa" + (multa + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"multa" + (multa + 0) + "\" value=\"0\"  readonly class=\"soloLectura\" /></td>\n");
                        } else {
                            row.append("<td><input type=\"text\" name=\"multa" + (multa + 0) + "\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"multa" + (multa + 0) + "\"  value=" + HTMLHelper.formatoMonto(pagos.get(multa).multa) + " /></td>\n");
                        }
                    }
                }

            }

            //	completa los pagos faltantes con ceros
            for (int j = multa; j < plazo; j++) {
                row.append("<td align='right'><input type=\"text\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"multa" + (j + 1) + "\"  value=" + 0 + " readOnly class=\"soloLectura\" /></td>\n");
            }
            row.append("</tr>\n");

            // AGREGAMOS ESPACIO			
            row.append("<tr><td colspan='19'>&nbsp</td></tr>");

            // TOTAL
            row.append("<tr>\n<td style=\"font-weight: bold\" colspan='2' align='right'>TOTAL</td>\n"
                    + //"<td width=\"20px\" style=\"font-size: small\">"+HTMLHelper.formatoMonto(montoTotalSemanal)+"</td>\n");
                    "<td width=\"20px\" style=\"font-size: small\">&nbsp</td>\n");
            int w = 0;
            for (w = 0; w < pagos.size(); w++) {
                if (corroborado[w]) {
                    row.append("<td  align='right' style=\"width: 50px\"><input type=\"text\" name=\"pagototal" + (w + 0) + "\" style=\"width: 100%;font-size: xx-small\" readonly class=\"soloLectura\" id=\"pagototal" + (w + 0) + "\" value=" + montoPagoRegistrado[w] + " /></td>\n");
                } else {
                    row.append("<td  align='right' style=\"width: 50px\"><input type=\"text\" name=\"pagototal" + (w + 0) + "\" style=\"width: 100%;font-size: xx-small\" readonly class=\"soloLectura\" id=\"pagototal" + (w + 0) + "\" value=\"0\" /></td>\n");
                }
            }

            //	completa los pagos faltantes con ceros
            for (int j = w; j < plazo; j++) {
                row.append("<td align='right' style=\"width: 50px\"><input type=\"text\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"pagototal" + (j + 1) + "\"  value=" + 0 + " readOnly class=\"soloLectura\" /></td>\n");
            }
            row.append("</tr>\n");
			//row.append(completarTabla( w , 15,"pagototal")+"</tr>\n");

            //ESPERADO	
            row.append("<tr>\n<td style=\"font-weight: bold\" colspan='2' align='right'>ESPERADO</td>\n"
                    + "<td align='right'>" + formatCantidad(montoTotalSemanal) + "</td>\n");

            //int z = 0;
            //double[] diferencia = new double[pagos.size()];
            for (w = 0; w < pagos.size(); w++) {
                row.append("<td style=\"width: 50px\"><input type=\"text\" name=\"esperado" + (w + 0) + "\" style=\"width: 100%;font-size: xx-small\" readonly class=\"soloLectura\" id=\"esperado" + (w + 0) + "\" value=" + HTMLHelper.formatoMonto(montoTotalSemanal) + " /></td>\n");
            }

            //	completa los pagos faltantes con ceros
            for (int j = w; j < plazo; j++) {
                row.append("<td style=\"width: 50px\"><input type=\"text\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"esperado" + (j + 1) + "\"  value=" + 0 + " readOnly class=\"soloLectura\" /></td>\n");
            }
            row.append("</tr>\n");

            //DIFERENCIA
            row.append("<tr>\n<td style=\"font-weight: bold\" colspan='2' align='right'>DIFERENCIA</td>\n"
                    + //"<td width=\"20px\" style=\"font-size: small\">"+HTMLHelper.formatoMonto(montoTotalEsperado-montoTotalSemanal)+"</td>\n");
                    "<td width=\"20px\" style=\"font-size: small\">&nbsp</td>\n");
            //DIFERENCIA POR PAGO
            for (w = 0; w < pagos.size(); w++) {
                row.append("<td align='right' style=\"width: 50px\"><input type=\"text\" name=\"diferencia" + (w + 0) + "\" style=\"width: 100%;font-size: xx-small\" readonly class=\"soloLectura\" id=\"diferencia" + (w + 0) + "\" value=" + HTMLHelper.formatoMonto(diferencia[w]) + " /></td>\n");
            }

            //	completa los pagos faltantes con ceros
            for (int j = w; j < plazo; j++) {
                row.append("<td align='right' style=\"width: 50px\"><input type=\"text\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"diferencia" + (j + 1) + "\"  value=" + 0 + " readOnly class=\"soloLectura\" /></td>\n");
            }
            row.append("</tr>\n");

            //REGISTRADO: se obtiene de la tabla grupal
            row.append("<tr>\n<td style=\"font-weight: bold\" colspan='2' align='right'>REGISTRADO</td>\n<td width=\"20px\" style=\"font-size: small\">&nbsp</td>\n");

            for (w = 0; w < pagos.size(); w++) {
                row.append("<td align='right' style=\"width: 50px\"><input type=\"text\" name=\"registrado" + (w + 0)
                        + "\" style=\"width: 100%;font-size: xx-small; \" readonly class=\"soloLectura\" id=\"registrado" + (w + 0)
                        + "\" value=\"" + HTMLHelper.formatoMonto(montoPagoRegistrado[w]) + "\" readonly/></td>\n");
            }

            //	completa los pagos faltantes con ceros
            for (int j = w; j < plazo; j++) {
                row.append("<td align='right' style=\"width: 50px\"><input type=\"text\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"registrado" + (j + 1) + "\"  value=" + 0 + " readOnly class=\"soloLectura\" /></td>\n");
            }

            //NUMERO DE PAGOS: se obtiene el numero de pagos.
            row.append("<tr>\n<td style=\"font-weight: bold\" colspan='2' align='right'>NO PAGOS</td>\n<td width=\"20px\" style=\"font-size: small\">&nbsp</td>\n");

            for (w = 0; w < pagos.size(); w++) {
                row.append("<td align='right' style=\"width: 50px\"><input type=\"text\" name=\"noPagos" + (w + 0)
                        + "\" style=\"width: 100%;font-size: xx-small; \" readonly class=\"soloLectura\" id=\"noPagos" + (w + 0)
                        + "\" value=\"" + HTMLHelper.displayField(numeroDePagos[w]) + "\" readonly/></td>\n");
            }

            //	completa los pagos faltantes con ceros
            for (int j = w; j < plazo; j++) {
                row.append("<td align='right' style=\"width: 50px\"><input type=\"text\" style=\"width: 100%;font-size: xx-small\" maxlength=\"10\" id=\"noPagos" + (j + 1) + "\"  value=" + 0 + " readOnly class=\"soloLectura\" /></td>\n");
            }

            //Mando el numero de pago en el que cayo por su fecha de pago y la tabla de amortizacion
            // JBL ENE/11, se cambia numAmortizacion+1 por numAmortizacion+0 para que concuerde con el numero de amortizacion del pago inicial en la base de datos
            int numAmortizacion = 0;
            for (numAmortizacion = 0; numAmortizacion < pagos.size(); numAmortizacion++) {
                row.append("<input type=\"hidden\" name=\"numPago" + (numAmortizacion + 0) + "\"   id=\"numPago" + (numAmortizacion + 0) + "\"  value=" + pagos.get(numAmortizacion).numPago + " />\n");

                row.append("<input type=\"hidden\" name=\"numAmortizacion" + (numAmortizacion + 0) + "\"  id=\"numAmortizacion" + (numAmortizacion + 0) + "\"  value=" + pagos.get(numAmortizacion).numAmortizacion + " />\n");

            }

            row.append("</tr>\n");
            row.append("\n<tr>\n<td><input type=\"hidden\" name=\"idGrupo\" value=\"" + ciclo.idGrupo + "\"></td>\n</tr>"
                    + " \n<tr>\n<td><input type=\"hidden\" name=\"idCiclo\" value=\"" + ciclo.idCiclo + "\"></td>\n</tr>");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return row.toString();
    }

    public static String completarTabla(int numExistente, int numDeseado, String nombre) {
        String res = "";
        char cad = '"';
        for (int x = numExistente; x <= numDeseado; x++) {
            res += "<td style=\"font-size: x-small; font-weight: bold\"  id=" + cad + nombre + (x + 1) + cad + ">0</td>\n";
        }
        return res;
    }

    public static String getDescripcion(TreeMap catalogo, String codigo) {

        String descripcion = "N/D";
        if (catalogo != null && catalogo.size() > 0 && codigo != null && !codigo.equals("")) {
            if (catalogo.containsKey(codigo)) {
                descripcion = (String) catalogo.get(codigo);
            }
        }
        return descripcion;

    }
    public static String getBotonInteciclo (int numgrupo, int numciclo){
        String html = "";
        
        return html;
    }

    public static String getCellColor(int calificacion) {

        String color = "#FFFFFF";
        switch (calificacion) {
            case 1:
                color = "#00E100";
                break;
            case 2:
                color = "#FF0000";
                break;
            case 3:
                color = "#BDBDBA";
                break;
            case 4:
                color = "#33FF99";
                break;
            default:
                color = "#FFFFFF";
                break;
        }
        return color;

    }
    public static String formatoMonto(int field) {

        DecimalFormat myFormatter = new DecimalFormat(ClientesConstants.FORMATO_MONTO);
        String output = "";

        if (field != 0) {
            output = myFormatter.format(field);
        } else {
            output = "0.00";
        }

        return output;

    }

}

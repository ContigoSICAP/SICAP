package com.sicap.clientes.helpers;

import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.TarjetaDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TarjetasVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class TarjetaHelper {
    
    private static Logger myLogger = Logger.getLogger(TarjetaHelper.class);
    private List<String> file = new ArrayList<String>();
    static final char numerico = '0';
    static final char alfanum = ' ';
    
    public ArrayList<TarjetasVO> getTarjetas(HttpServletRequest request){
        
        ArrayList<TarjetasVO> arrTarjetas = new ArrayList<TarjetasVO>();
        String[] idTarjetas = null;
        BitacoraUtil bitacora = null;
        SucursalVO sucursal = null;
        try {
            idTarjetas = request.getParameterValues("idCheckBox");
            if (idTarjetas != null) {
                for (int i = 0; i < idTarjetas.length; i++) {
                    sucursal = new SucursalVO();
                    sucursal.setNombre(HTMLHelper.getParameterString(request, "nomSucursal"+idTarjetas[i]));
                    arrTarjetas.add(new TarjetasVO(HTMLHelper.getParameterString(request, "numTarjeta"+idTarjetas[i]), 
                            new ClienteVO(HTMLHelper.getParameterInt(request, "numCliente"+idTarjetas[i]), HTMLHelper.getParameterString(request, "nombre"+idTarjetas[i]), HTMLHelper.getParameterString(request, "paterno"+idTarjetas[i]), HTMLHelper.getParameterString(request, "materno"+idTarjetas[i]), HTMLHelper.getParameterString(request, "rfc"+idTarjetas[i]), HTMLHelper.getParameterSqlDate(request, "fechaNac"+idTarjetas[i])), 
                            sucursal));
                    bitacora = new BitacoraUtil(HTMLHelper.getParameterInt(request, "numCliente"+idTarjetas[i]), request.getRemoteUser(), "CommandPersonalizacionTarjeta");
                    bitacora.registraEventoString("numTarjeta="+request.getParameter("numTarjeta"+idTarjetas[i])+", idCliente="+request.getParameter("numCliente"+idTarjetas[i])+", nombre="+request.getParameter("nombre"+idTarjetas[i])+" "+request.getParameter("paterno"+idTarjetas[i])+" "+request.getParameter("materno"+idTarjetas[i])+" rfc="+request.getParameter("rfc"+idTarjetas[i]));
                }
            }
        } catch (Exception e) {
            myLogger.error("Error ", e);
        }
        return arrTarjetas;
    }
    
    public ArrayList<TarjetasVO> getTarjetasFondeo(HttpServletRequest request){
        
        ArrayList<TarjetasVO> arrTarjetas = new ArrayList<TarjetasVO>();
        String[] idTarjetas = null;
        BitacoraUtil bitacora = null;
        SucursalVO sucursal = null;
        try {
            idTarjetas = request.getParameterValues("idCheckBox");
            if (idTarjetas != null) {
                for (int i = 0; i < idTarjetas.length; i++) {
                    sucursal = new SucursalVO();
                    sucursal.setNombre(HTMLHelper.getParameterString(request, "nomSucursal"+idTarjetas[i]));
                    sucursal.setIdSucursal(HTMLHelper.getParameterInt(request, "idSucursal"+idTarjetas[i]));
                    arrTarjetas.add(new TarjetasVO(HTMLHelper.getParameterInt(request, "identificacion"+idTarjetas[i]), null, request.getRemoteUser(), HTMLHelper.getParameterInt(request, "banco"), HTMLHelper.getParameterString(request, "tarjeta"+idTarjetas[i]), HTMLHelper.getParameterInt(request, "idCliente"+idTarjetas[i]),
                            0, null, "", HTMLHelper.getParameterInt(request, "idSolicitud"+idTarjetas[i]), sucursal, HTMLHelper.getParameterString(request, "nombre"+idTarjetas[i]), HTMLHelper.getParameterDouble(request, "importe"+idTarjetas[i]), HTMLHelper.getParameterInt(request, "estatus"+idTarjetas[i]), 
                            HTMLHelper.getParameterString(request, "grupo"+idTarjetas[i]), ""));
                    bitacora = new BitacoraUtil(HTMLHelper.getParameterInt(request, "idCliente"+idTarjetas[i]), request.getRemoteUser(), "CommandFondeoTarjetas");
                    bitacora.registraEventoString("numTarjeta="+request.getParameter("tarjeta"+idTarjetas[i])+", idCliente="+request.getParameter("idCliente"+idTarjetas[i])+", nombre="+request.getParameter("nombre"+idTarjetas[i])+" idSolicitud="+request.getParameter("idSolicitud"+idTarjetas[i])+" monto="+request.getParameter("importe"+idTarjetas[i]));
                }
            }
        } catch (Exception e) {
            myLogger.error("Error ", e);
        }
        return arrTarjetas;
    }
    
    public List creaArchivoPersonalizacionBanamex(ArrayList<TarjetasVO> arrTarjetas, int numEnvio, int banco) throws ClientesException, SQLException{
        
        ClienteDAO clienteDAO = new ClienteDAO();
        DireccionDAO dirDAO = new DireccionDAO();
        TarjetaDAO tarjetaDAO = new TarjetaDAO();
        ClienteVO clienteVO = new ClienteVO();
        Connection con = null;
        boolean repetida= false;
        try {
            con = ConnectionManager.getMySQLConnection();
            con.setAutoCommit(false);
            encabezadoPersonalizacionBanamex(numEnvio, arrTarjetas.size());
            for (TarjetasVO tarjeta : arrTarjetas) {
                if(tarjetaDAO.verificaEnvioTarjeta(tarjeta, banco) == 0){
                    clienteVO = clienteDAO.getClienteRFC(tarjeta.getClienteVO().getRfc());
                    clienteVO.setDirecciones(dirDAO.getDirecciones(tarjeta.getClienteVO().getIdCliente()));
                    detallePersonalizacionBanamex(tarjeta, clienteVO);
                    tarjetaDAO.updateAsignaTarjeta(tarjeta, numEnvio, banco, con);
                } else {
                    repetida = true;
                    break;
                }
            }
            if(!repetida)
                con.commit();
            else{
                con.rollback();
                file.clear();
            }
        } catch (Exception e) {
            con.rollback();
            myLogger.error("Error ", e);
        } finally{
            if(con!=null){
                con.close();
            }
        }
        return file;
    }
    
    public List creaArchivoFondeoBanamex(ArrayList<TarjetasVO> arrTarjetas, int numEnvio, int banco) throws ClientesException, SQLException{
        
        TarjetaDAO tarjetaDAO = new TarjetaDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        Connection con = null;
        boolean repetida= false;
        TarjetasVO tarjetaVO = new TarjetasVO();
        ClienteVO clienteVO = new ClienteVO();
        try {
            con = ConnectionManager.getMySQLConnection();
            con.setAutoCommit(false);
            encabezadoFondeoBanamex(numEnvio, arrTarjetas.size());
            tarjetaVO = sumaTotales(arrTarjetas);
            registroGlobalBanamex(tarjetaVO);
            for (TarjetasVO tarjeta : arrTarjetas) {
                if(tarjetaDAO.verificaEnvioFondeoTarjeta(tarjeta, banco) == 40){
                    clienteVO = clienteDAO.getCliente(tarjeta.getIdCliente());
                    detalleFondeoBanamex(tarjeta, clienteVO);
                    tarjeta.setEstatus(ClientesConstants.OP_DISPERSADA);
                    tarjetaDAO.updateFondeoTarjeta(tarjeta, numEnvio, banco, con);
                } else {
                    repetida = true;
                    break;
                }
            }
            registroControlBanamex(tarjetaVO);
            if(!repetida)
                con.commit();
            else{
                con.rollback();
                file.clear();
            }
        } catch (Exception e) {
            con.rollback();
            myLogger.error("Error ", e);
        } finally{
            if(con!=null){
                con.close();
            }
        }
        return file;
    }
    
    private String encabezadoPersonalizacionBanamex(int envio, int totTarjetas) {

        StringBuffer buffer = new StringBuffer();
        String CVE_BANAMEX = CatalogoHelper.getParametro("CVE_EMISORA_BANAMEX");
        try {
            String fecha = Convertidor.dateToString(new Date(), "yyyyMMdd");
            buffer.append(FormatUtil.completaCadena("1", numerico, 1, "L")); //TIPO DE REGISTRO
            buffer.append(FormatUtil.completaCadena("100607780", numerico, 12, "L"));//NUMERO DE CLIENTE
            buffer.append(FormatUtil.completaCadena(fecha, numerico, 8, "L")); //FECHA DE ALTA
            buffer.append(FormatUtil.completaCadena(envio + "", numerico, 4, "L")); //SECUENCIAL DEL ARCHIVO
            buffer.append(FormatUtil.completaCadena(totTarjetas + "", numerico, 6, "L")); //TOTAL DE ALTAS
            buffer.append(FormatUtil.completaCadena("", alfanum, 460, "L")); //FILLER
            //System.out.println("buffer.toString() "+buffer.toString());
            file.add(buffer.toString());
        } catch (Exception e) {
            myLogger.error("Error ", e);
        }
        return buffer.toString();
    }
    
    private String encabezadoFondeoBanamex(int envio, int totTarjetas) {

        StringBuffer buffer = new StringBuffer();
        String CVE_BANAMEX = CatalogoHelper.getParametro("CVE_EMISORA_BANAMEX");
        try {
            String fecha = Convertidor.dateToString(new Date(), "yyMMdd");
            buffer.append(FormatUtil.completaCadena("1", numerico, 1, "L")); //TIPO DE REGISTRO
            buffer.append(FormatUtil.completaCadena("100607780", numerico, 12, "L"));//NUMERO DE IDENTIFICACION DEL CLIENTE
            buffer.append(FormatUtil.completaCadena(fecha, numerico, 6, "L")); //FECHA DE PAGO
            buffer.append(FormatUtil.completaCadena(envio + "", numerico, 4, "L")); //SECUENCIAL DEL ARCHIVO
            buffer.append(FormatUtil.completaCadena("CEGE CAPITAL SAPI DE CV SOFOM ENR", alfanum, 36, "R")); //NOMBRE DE LA EMPRESA
            buffer.append(FormatUtil.completaCadena("No "+envio, alfanum, 20, "L")); //DESCRIPCION DEL ARCHIVO
            buffer.append(FormatUtil.completaCadena("15", numerico, 2, "L")); //NATURALEZA DEL ARCHIVO
            buffer.append(FormatUtil.completaCadena("D", alfanum, 1, "L")); //VARSION DEL LAYOUT
            buffer.append(FormatUtil.completaCadena("01", numerico, 2, "L")); //TIPO DE CARGO
            //System.out.println("buffer.toString() "+buffer.toString());
            file.add(buffer.toString());
        } catch (Exception e) {
            myLogger.error("Error ", e);
        }
        return buffer.toString();
    }
    
    private String registroControlBanamex(TarjetasVO tarjeta) {

        StringBuffer buffer = new StringBuffer();
        String totalArchivo = FormatUtil.formatDoble("###00", tarjeta.getMonto() * 100);
        try {
            String fecha = Convertidor.dateToString(new Date(), "yyMMdd");
            buffer.append(FormatUtil.completaCadena("4", numerico, 1, "L")); //TIPO DE REGISTRO
            buffer.append(FormatUtil.completaCadena("001", numerico, 3, "L"));//CLAVE DE MONEDA
            buffer.append(FormatUtil.completaCadena(tarjeta.getEnvio()+"", numerico, 6, "L")); //NUMERO DE ABONOS
            buffer.append(FormatUtil.completaCadena(totalArchivo+"", numerico, 18, "L")); //IMPORTE TOTAL DE ABONOS
            buffer.append(FormatUtil.completaCadena("1", numerico, 6, "L")); //NUMERO DE CARGOS
            buffer.append(FormatUtil.completaCadena(totalArchivo+"", numerico, 18, "L")); //IMPORTE TOTAL DE CARGOS
            //System.out.println("buffer.toString() "+buffer.toString());
            file.add(buffer.toString());
        } catch (Exception e) {
            myLogger.error("Error ", e);
        }
        return buffer.toString();
    }
    
    private String detallePersonalizacionBanamex(TarjetasVO tarjeta, ClienteVO cliente) throws Exception {

        StringBuffer buffer = new StringBuffer();
        String nomCompleto = FormatUtil.eliminaCaracteresInvalidos(cliente.getNombre().trim())+","+FormatUtil.eliminaCaracteresInvalidos(cliente.getaPaterno().trim())+"/"+FormatUtil.eliminaCaracteresInvalidos(cliente.getaMaterno().trim());
        int difString = 0;
        if(nomCompleto.length()>55){
            difString = nomCompleto.length()-55;
            cliente.setNombre(cliente.getNombre().trim());
            cliente.setNombre(cliente.getNombre().substring(0, cliente.getNombre().length()-difString));
            nomCompleto = cliente.getNombre().trim()+","+cliente.getaPaterno().trim()+"/"+cliente.getaMaterno().trim();
        }
        String fecNac = Convertidor.dateToString(cliente.getFechaNacimiento(), "yyyyMMdd");
        fecNac = fecNac.replace("-", "");
        String direccion = cliente.direcciones[0].calle.trim()+" "+cliente.direcciones[0].numeroExterior.trim();
        if(direccion.length()>36){
            difString = direccion.length()-36;
            direccion = direccion.substring(0, direccion.length()-difString);
        }
        direccion = direccion.toUpperCase();
        String colonia = cliente.direcciones[0].colonia.trim();
        if(colonia.length()>24){
            difString = colonia.length()-24;
            colonia = colonia.substring(0, colonia.length()-difString);
        }
        colonia = colonia.toUpperCase();
        String municipio = cliente.direcciones[0].municipio.trim();
        if(municipio.length()>20){
            difString = municipio.length()-20;
            municipio = municipio.substring(0, municipio.length()-difString);
        }
        municipio = municipio.toUpperCase();
        try {
            String fecha = Convertidor.dateToString(new Date(), "yyyyMMdd");
            buffer.append(FormatUtil.completaCadena("2", numerico, 1, "L")); //TIPO DE REGISTRO
            buffer.append(FormatUtil.completaCadena("1", numerico, 1, "L"));//TIPO DE PRODUCTO
            buffer.append(FormatUtil.completaCadena("1", numerico, 1, "L")); //TIPO DE ENTREGA
            buffer.append(FormatUtil.completaCadena("1", numerico, 16, "L")); //NUMERO DE UNIDAD DE TRABAJO EJEMPLO ENVIADO ERRONEOA 1 SE PONE TARJETA
            buffer.append(FormatUtil.completaCadena("01" + "", numerico, 2, "L")); //TIPO DE PERSONA
            buffer.append(FormatUtil.completaCadena(nomCompleto, alfanum, 55, "R")); //NOMBRE DEL EMPLEADO
            buffer.append(FormatUtil.completaCadena(fecNac, numerico, 8, "L")); //FECHA DE NACIMIENTO
            buffer.append(FormatUtil.completaCadena(FormatUtil.eliminaCaracteresInvalidos(direccion), alfanum, 36, "R")); //CALLE Y NUMERO
            buffer.append(FormatUtil.completaCadena(FormatUtil.eliminaCaracteresInvalidos(colonia), alfanum, 24, "R")); //COLONIA
            buffer.append(FormatUtil.completaCadena(cliente.direcciones[0].cp, numerico, 6, "L")); //CODIGO POSTAL
            buffer.append(FormatUtil.completaCadena(FormatUtil.eliminaCaracteresInvalidos(municipio), alfanum, 20, "R")); //POBLACION O DELEGACION
            buffer.append(FormatUtil.completaCadena(getEstadosBanamex(cliente.direcciones[0].estado), numerico, 2, "L")); //ESTADO
            buffer.append(FormatUtil.completaCadena(tarjeta.getTarjeta(), numerico, 16, "L")); //NUMERO DE TARJETA
            buffer.append(FormatUtil.completaCadena("2", numerico, 1, "L")); //FORMA DE PAGO
            buffer.append(FormatUtil.completaCadena("0", numerico, 1, "L")); //ASIGNACION DE PAGO
            buffer.append(FormatUtil.completaCadena("0001", numerico, 4, "L")); //NACIONALIDAD
            buffer.append(FormatUtil.completaCadena("", alfanum, 297, "L")); //FILLER
            //System.out.println("buffer.toString() "+buffer.toString());
            file.add(buffer.toString());
        } catch (Exception e) {
            myLogger.error("Error ", e);
        }
        return buffer.toString();
    }
    
    private String registroGlobalBanamex(TarjetasVO tarjeta) throws Exception {

        StringBuffer buffer = new StringBuffer();
        try {
            String fecha = Convertidor.dateToString(new Date(), "yyyyMMdd");
            String totalArchivo = FormatUtil.formatDoble("###00", tarjeta.getMonto() * 100);
            String totalEnvios = tarjeta.getEnvio()+"";
            
            buffer.append(FormatUtil.completaCadena("2", numerico, 1, "L")); //TIPO DE REGISTRO
            buffer.append(FormatUtil.completaCadena("1", numerico, 1, "L"));//TIPO DE OPERACION
            buffer.append(FormatUtil.completaCadena("001", numerico, 3, "L")); //CLAVE DE MONEDA
            buffer.append(FormatUtil.completaCadena(totalArchivo + "", numerico, 18, "L")); //IMPORTE A CARGAR
            buffer.append(FormatUtil.completaCadena("01", numerico, 2, "L")); //TIPO DE CUENTA
            buffer.append(FormatUtil.completaCadena("70061443957", numerico, 20, "L")); //NUMERO DE CUENTA 002180700614439573
            buffer.append(FormatUtil.completaCadena(totalEnvios, numerico, 6, "L")); //TOTAL DE ABONOS
            //System.out.println("buffer.toString() "+buffer.toString());
            file.add(buffer.toString());
        } catch (Exception e) {
            myLogger.error("Error ", e);
        }
        return buffer.toString();
    }
    
    private String detalleFondeoBanamex(TarjetasVO tarjeta, ClienteVO cliente) throws Exception {

        StringBuffer buffer = new StringBuffer();
        try {
            String fecha = Convertidor.dateToString(new Date(), "yyyyMMdd");
            String monto = FormatUtil.formatDoble("###00", tarjeta.getMonto() * 100);
            String nomCompleto = FormatUtil.eliminaCaracteresInvalidos(cliente.getNombre().trim())+","+FormatUtil.eliminaCaracteresInvalidos(cliente.getaPaterno().trim())+"/"+FormatUtil.eliminaCaracteresInvalidos(cliente.getaMaterno().trim());
            int difNombre = 0;
            if(nomCompleto.length()>55){
                difNombre = nomCompleto.length()-55;
                cliente.setNombre(cliente.getNombre().trim());
                cliente.setNombre(cliente.getNombre().substring(0, cliente.getNombre().length()-difNombre));
                nomCompleto = FormatUtil.eliminaCaracteresInvalidos(cliente.getNombre().trim())+","+FormatUtil.eliminaCaracteresInvalidos(cliente.getaPaterno().trim())+"/"+FormatUtil.eliminaCaracteresInvalidos(cliente.getaMaterno().trim());
            }
            String nomEquipo = tarjeta.getGrupo();
            if(nomEquipo.length()>15){
                nomEquipo = tarjeta.getGrupo().substring(0, 15);
            }
            
            buffer.append(FormatUtil.completaCadena("3", numerico, 1, "L")); //TIPO DE REGISTRO
            buffer.append(FormatUtil.completaCadena("0", numerico, 1, "L"));//TIPO DE OPERACION
            buffer.append(FormatUtil.completaCadena("001", numerico, 3, "L")); //METODO DE PAGO
            buffer.append(FormatUtil.completaCadena("01", numerico, 2, "L")); //TIPO DE PAGO
            buffer.append(FormatUtil.completaCadena("001", numerico, 3, "L")); //CLAVE DE MONEDA
            buffer.append(FormatUtil.completaCadena(monto, numerico, 18, "L")); //IMPORTE
            buffer.append(FormatUtil.completaCadena("03", numerico, 2, "L")); //TIPO DE CUENTA DE ABONO
            buffer.append(FormatUtil.completaCadena(tarjeta.getTarjeta(), numerico, 20, "L")); //NUMERO DE CUENTA ABONO
            buffer.append(FormatUtil.completaCadena("CLIENTE "+tarjeta.getIdCliente(), alfanum, 16, "R")); //REFERENCIA DEL PAGO
            buffer.append(FormatUtil.completaCadena(nomCompleto, alfanum, 55, "R")); //BENEFICIARIO
            buffer.append(FormatUtil.completaCadena("CREDITO EQUIPO "+FormatUtil.eliminaCaracteresInvalidos(nomEquipo), alfanum, 35, "L")); //REFERENCIA 1
            buffer.append(FormatUtil.completaCadena("FINANCIERA CONTIGO", alfanum, 35, "L")); //REFERENCIA 2
            buffer.append(FormatUtil.completaCadena("", alfanum, 35, "L")); //REFERENCIA 3
            buffer.append(FormatUtil.completaCadena("", alfanum, 35, "L")); //REFERENCIA 4
            buffer.append(FormatUtil.completaCadena("0", numerico, 4, "L")); //CLAVE DE BANCO
            buffer.append(FormatUtil.completaCadena("0", numerico, 2, "L")); //PLAZO
            buffer.append(FormatUtil.completaCadena("", alfanum, 14, "L")); //RFC
            buffer.append(FormatUtil.completaCadena("", alfanum, 8, "L")); //IVA
            buffer.append(FormatUtil.completaCadena("", alfanum, 80, "L")); //PARA USO FUTURO
            buffer.append(FormatUtil.completaCadena("", alfanum, 50, "L")); //PARA USO FUTURO
            //System.out.println("buffer.toString() "+buffer.toString());
            file.add(buffer.toString());
        } catch (Exception e) {
            myLogger.error("Error ", e);
        }
        return buffer.toString();
    }
    
    public String getFileName(String filename, int envio) {
        try {

            String fecha = Convertidor.dateToString(new Date(), "yyyyMMdd");

            filename += fecha.substring(2, 4);
            filename += fecha.substring(4, 6);
            filename += fecha.substring(6, 8);
            filename += FormatUtil.completaCadena(envio + "", numerico, 4, "L");
            filename += ".txt";

        } catch (Exception e) {
            myLogger.error("Error ", e);
        }
        return filename;
    }
    
    private String getEstadosBanamex(String estado){
        
        String num = "00";
        if(estado.equals("Distrito Federal"))
            num = "01";
        else if(estado.equals("Aguascalientes"))
            num = "02";
        else if(estado.equals("Baja California"))
            num = "03";
        else if(estado.equals("Baja California Sur"))
            num = "04";
        else if(estado.equals("Campeche"))
            num = "05";
        else if(estado.equals("Coahuila"))
            num = "06";
        else if(estado.equals("Colima"))
            num = "07";
        else if(estado.equals("Chiapas"))
            num = "08";
        else if(estado.equals("Chihuahua"))
            num = "09";
        else if(estado.equals("Durango"))
            num = "10";
        else if(estado.equals("Guanajuato"))
            num = "11";
        else if(estado.equals("Guerrero"))
            num = "12";
        else if(estado.equals("Hidalgo"))
            num = "13";
        else if(estado.equals("Jalisco"))
            num = "14";
        else if(estado.equals("México"))
            num = "15";
        else if(estado.equals("Michoacán"))
            num = "16";
        else if(estado.equals("Morelos"))
            num = "17";
        else if(estado.equals("Nayarit"))
            num = "18";
        else if(estado.equals("Nuevo León"))
            num = "19";
        else if(estado.equals("Oaxaca"))
            num = "20";
        else if(estado.equals("Puebla"))
            num = "21";
        else if(estado.equals("Querétaro"))
            num = "22";
        else if(estado.equals("Quintana Roo"))
            num = "23";
        else if(estado.equals("San Luís Potosí"))
            num = "24";
        else if(estado.equals("Sinaloa"))
            num = "25";
        else if(estado.equals("Sonora"))
            num = "26";
        else if(estado.equals("Tabasco"))
            num = "27";
        else if(estado.equals("Tamaulipas"))
            num = "28";
        else if(estado.equals("Tlaxcala"))
            num = "29";
        else if(estado.equals("Veracruz"))
            num = "30";
        else if(estado.equals("Yucatán"))
            num = "31";
        else if(estado.equals("Zacatecas"))
            num = "32";
        return num;
    }
    
    private TarjetasVO sumaTotales(ArrayList<TarjetasVO> arrTarjetas){
        
        TarjetasVO tarjeta = new TarjetasVO();
        int totEnvio = 0;
        double importe = 0.00;
        for (TarjetasVO tarjetasVO : arrTarjetas) {
            totEnvio++;
            importe+= tarjetasVO.getMonto();
        }
        tarjeta.setEnvio(totEnvio);
        tarjeta.setMonto(importe);
        return tarjeta;
    }
}

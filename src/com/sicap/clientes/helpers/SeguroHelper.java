package com.sicap.clientes.helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.SeguroConstantes;
import com.sicap.clientes.vo.AseguradosVO;
import com.sicap.clientes.vo.BeneficiarioVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.SegurosVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import java.util.List;

public class SeguroHelper {

    public static SegurosVO getSeguroVO(SegurosVO seguro, HttpServletRequest request) throws Exception {

        seguro.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
        seguro.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
/*      if(HTMLHelper.getParameterString(request, "numSeguro").equals("")){
            SegurosDAO numSeguros = new SegurosDAO();
            seguro.numSeguro = numSeguros.numSeguro(seguro.idCliente);
            if(seguro.numSeguro==0)
                seguro.numSeguro = 1;
            else
                seguro.numSeguro++;
        }else
            seguro.numSeguro = HTMLHelper.getParameterInt(request, "numSeguro");
*/
        seguro.sumaAsegurada = HTMLHelper.getParameterInt(request, "sumaAsegurada");
        seguro.modulos = HTMLHelper.getParameterInt(request, "modulos");
        seguro.contratacion = HTMLHelper.getParameterInt(request, "contratacion");
        seguro.fechaFirma = Convertidor.stringToSqlDate(request.getParameter("fechaFirma"));
        seguro.comentarios = request.getParameter("comentarios");
        seguro.asegurados = getAsegurados(request);
        seguro.beneficiarios = getBeneficiarios(request);
        seguro.banco = HTMLHelper.getParameterInt(request, "banco");
        seguro.cuenta = HTMLHelper.getParameterInt(request, "cuenta");
        return seguro;
    }

    public static List<BeneficiarioVO> getBeneficiarios(HttpServletRequest request) {
        
        ArrayList<BeneficiarioVO> arrBeneficiarios = new ArrayList<BeneficiarioVO>();
        String fecNacimiento = "";

        String nombres[] = request.getParameterValues("nomBeneficiario");
        String apPaternos[] = request.getParameterValues("apPaterBene");
        String apMaternos[] = request.getParameterValues("apMaterBene");
        String arrFecNacimiento[] = request.getParameterValues("fecNaciBene");
        String relacion[] = request.getParameterValues("relacion");
        String otraRelacion[] = request.getParameterValues("especificacion");
        String porcentaje[] = request.getParameterValues("porcentaje");
        if(request.getParameter("contratacion").equals("1")){
            for (int i = 0; i < request.getParameterValues("nomBeneficiario").length; i++) {
                if (nombres[i] != null && !nombres[i].equals("")) {
                    fecNacimiento = arrFecNacimiento[i].substring(6, 10)+"-"+arrFecNacimiento[i].substring(3, 5)+"-"+arrFecNacimiento[i].substring(0, 2);

                    arrBeneficiarios.add(new BeneficiarioVO(nombres[i], apPaternos[i], apMaternos[i], Integer.valueOf(relacion[i]), otraRelacion[i], Integer.valueOf(porcentaje[i]), fecNacimiento));
                }
            }
        }
        return arrBeneficiarios;
    }
    
    public static List<AseguradosVO> getAsegurados(HttpServletRequest request) {

        ArrayList<AseguradosVO> arrAsegurados = new ArrayList<AseguradosVO>();
        String fecNacimiento = "";

        String nombres[] = request.getParameterValues("nomAsegurado");
        String apPaternos[] = request.getParameterValues("apPaterAseg");
        String apMaternos[] = request.getParameterValues("apMaterAseg");
        String arrFecNacimiento[] = request.getParameterValues("fecNacimiento");
        String parentesco[] = request.getParameterValues("parentesco");
        if(request.getParameter("contratacion").equals("1")){
            for (int i = 0; i < request.getParameterValues("nomAsegurado").length; i++) {
                if (nombres[i] != null && !nombres[i].equals("")) {
                    fecNacimiento = arrFecNacimiento[i].substring(6, 10)+"-"+arrFecNacimiento[i].substring(3, 5)+"-"+arrFecNacimiento[i].substring(0, 2);
                    arrAsegurados.add(new AseguradosVO(nombres[i], apPaternos[i], apMaternos[i], fecNacimiento, Integer.parseInt(parentesco[i])));
                }
            }
        }
        return arrAsegurados;
    }

    public static Double getMontoPeriodo(int frecuenciaPago, int tipoSuma, int modulos) throws Exception {

        int factor = 1;
        double montoSeguro = 0.00;

        if (frecuenciaPago == ClientesConstants.PAGO_SEMANAL) {
            factor = 4;
        } else if (frecuenciaPago == ClientesConstants.PAGO_QUINCENAL) {
            factor = 2;
        }

        switch (tipoSuma) {
            case (1):
                montoSeguro = (9.00 / factor) * modulos;
                break;
            case (2):
                montoSeguro = (17.50 / factor) * modulos;
                break;
            case (3):
                montoSeguro = (28.50 / factor) * modulos;
                break;
        }

        return montoSeguro;

    }

    public static boolean clientePuedeContratarSeguro(ClienteVO cliente) {
        boolean resultado = false;
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.YEAR, -66);
        Date fechaLimite = calendario.getTime();
        if (cliente.fechaNacimiento != null && cliente.fechaNacimiento.after(fechaLimite)) {
            resultado = true;
        }
        return resultado;
    }

    public static boolean cicloSegurosCompletos(CicloGrupalVO ciclo) {
        boolean resultado = true;
        SegurosVO[] seguros = null;
        ArrayList<SegurosVO> array = new ArrayList<SegurosVO>();

        int numSeguros = 0;

        try {
            for (int i = 0; i < ciclo.integrantes.length; i++) {
                SegurosVO seguro = null;
                seguro = new SegurosDAO().getSeguros(ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud);
                if (seguro != null) {
                    numSeguros++;
                }
                array.add(seguro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (array != null && array.size() > 0) {
            seguros = new SegurosVO[array.size()];
            for (int i = 0; i < seguros.length; i++) {
                seguros[i] = (SegurosVO) array.get(i);
            }
        }
        if (seguros != null) {
            for (int i = 0; i < seguros.length; i++) {
//		    	if ( seguros[i]!=null && seguros[i].contratacion == 2)
//					resultado = false;
            }
        } else {
            resultado = false;
        }

        if (ciclo.integrantes.length != numSeguros) {
            resultado = false;
        }

        return resultado;
    }

    public static double obtenPrimaMensual(SolicitudVO solicitud) {

        double primaMensual = 0;
        switch (solicitud.seguro.sumaAsegurada) {
            case 1:
                primaMensual = 15 * solicitud.seguro.modulos;
                break;
            case 2:
                primaMensual = 30 * solicitud.seguro.modulos;
                break;
            case 3:
                primaMensual = 50 * solicitud.seguro.modulos;
                break;
        }
        return primaMensual;
    }

    public static double obtenPrimaperiodoPago(SolicitudVO solicitud) {
        double primaMensual = obtenPrimaMensual(solicitud);
        double primaPeriodo = 0;

        switch (solicitud.decisionComite.frecuenciaPago) {
            case (ClientesConstants.PAGO_SEMANAL):
                primaPeriodo = primaMensual / 4;
                break;
            case ClientesConstants.PAGO_QUINCENAL:
                primaPeriodo = primaMensual / 2;
                break;
            default:
                primaPeriodo = primaMensual;
        }
        return primaPeriodo;
    }
     public static void calculaPrimaTotal(SolicitudVO solicitud) {

        double primaMensual = 0;

        if (solicitud.decisionComite != null) {

            switch (solicitud.seguro.sumaAsegurada) {
                case 1:
                    primaMensual = 25 * solicitud.seguro.modulos;
                    break;
                case 2:
                    primaMensual = 15 * solicitud.seguro.modulos;
                    break;
                case 3:
                    primaMensual = 30 * solicitud.seguro.modulos;
                    break;
                case 4:
                    primaMensual = 50 * solicitud.seguro.modulos;
                    break;
            }
            //VARIABLE FIJA MIENTRAS SE DEFINE EL USO DE LOS SEGUROS Y COMO SE MANEJARAN 2012/02/08
            primaMensual = 25;
            
            switch (solicitud.seguro.modulos) {
                case 1:
                    //solicitud.seguro.primaTotal = primaMensual * (solicitud.decisionComite.plazoAutorizado / 4);
                    solicitud.seguro.primaTotal = 110;
                    break;                
                default:
                    solicitud.seguro.primaTotal = primaMensual * solicitud.decisionComite.plazoAutorizado;
            }
        }

    }

    public static void calculaPrimaTotal(SolicitudVO solicitud, SucursalVO sucursalVo) {

        double primaMensual = 0;

        if (solicitud.decisionComite != null) {

            switch (solicitud.seguro.sumaAsegurada) {
                case 1:
                    primaMensual = 25 * solicitud.seguro.modulos;
                    break;
                case 2:
                    primaMensual = 15 * solicitud.seguro.modulos;
                    break;
                case 3:
                    primaMensual = 30 * solicitud.seguro.modulos;
                    break;
                case 4:
                    primaMensual = 50 * solicitud.seguro.modulos;
                    break;
            }
            //VARIABLE FIJA MIENTRAS SE DEFINE EL USO DE LOS SEGUROS Y COMO SE MANEJARAN 2012/02/08
            primaMensual = 25;
            
            switch (solicitud.seguro.modulos) {
                case 1:
                    //solicitud.seguro.primaTotal = primaMensual * (solicitud.decisionComite.plazoAutorizado / 4);
                    solicitud.seguro.primaTotal = 110;
                    break;
                case 2:
                    solicitud.seguro.primaTotal = sucursalVo.SeguroFunerario;
                    break;
                default:
                    solicitud.seguro.primaTotal = primaMensual * solicitud.decisionComite.plazoAutorizado;
            }
        }

    }

    public static double obtenPrimaConComision(SolicitudVO solicitud, TreeMap catComisiones) {

        double resultado = 0;

        if (solicitud.seguro != null && solicitud.seguro.primaTotal != 0) {
            resultado = ClientesUtil.calculaMontoConComision(solicitud.seguro.primaTotal, solicitud.decisionComite.comision, catComisiones);
        }
        return resultado;
    }
    
    /**
     * Obtiene el costo del seguro con base en las opciones del catalogo de
     * seguros clientes_cec.c_tipo_seguro.
     * 
     * @author axxis
     * @since Agosto 2017
     * @version 1.0v
     * 
     * @param tipoSeguro tipo de seguro seleccionado
     * @param sucursalVo contiene el valor del seguro funenario para la sucursal
     * 
     * @return costo del seguro contratado
     */
    public static double getCostoSeguro(int tipoSeguro, SucursalVO sucursalVo) {
        double costoSeguroContratado;
        switch (tipoSeguro) {
            case SeguroConstantes.SEG_VIDA:
                costoSeguroContratado = SeguroConstantes.COSTO_SEG_VIDA;
                break;
            case SeguroConstantes.SEG_FUNERARIO:
                costoSeguroContratado = sucursalVo.SeguroFunerario;
                break;
            default:
                costoSeguroContratado = 0.0;
        }
        return costoSeguroContratado;
    }
}
package com.sicap.clientes.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.vo.BusquedaClientesVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ComisionVO;
import com.sicap.clientes.vo.DecisionComiteVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.SolicitudVO;
import java.util.ArrayList;

public class ClientesUtil {

    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(ClienteDAO.class);

    public static void cambiaEstatusSolicitud(ClienteVO cliente, int idSolicitud) throws ClientesException {
        SolicitudDAO solicituddao = new SolicitudDAO();
        if (cliente.solicitudes[idSolicitud].decisionComite.decisionComite == ClientesConstants.CREDITO_RECHAZADO) {
            cliente.solicitudes[idSolicitud].estatus = ClientesConstants.SOLICITUD_RECHAZADA;
        } else {
            cliente.solicitudes[idSolicitud].estatus = ClientesConstants.SOLICITUD_AUTORIZADA;
        }
        solicituddao.updateSolicitud(cliente.idCliente, cliente.solicitudes[idSolicitud]);

    }

    //public static int asignaEstatusSolicitud(ClienteVO cliente, int idSolicitud) throws ClientesException {
    public static int asignaEstatusSolicitud(SolicitudVO solicitud, int idSolicitud) throws ClientesException {

        //if (cliente.solicitudes[idSolicitud].decisionComite.decisionComite == ClientesConstants.CREDITO_RECHAZADO) {
        if (solicitud.decisionComite.decisionComite == ClientesConstants.CREDITO_RECHAZADO) {
            return ClientesConstants.SOLICITUD_RECHAZADA;
        } else {
            return ClientesConstants.SOLICITUD_AUTORIZADA;
        }

    }

    public static boolean clienteCompletoMicro(ClienteVO cliente, int indiceSolicitud, Vector<Notification> notificaciones) {

        if (cliente.sexo == 0) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Datos generales"));
        }
        if (cliente.solicitudes[indiceSolicitud].obligadosSolidarios == null || cliente.solicitudes[indiceSolicitud].obligadosSolidarios.length == 0) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar al menos un Obligado Solidario"));
        } else {
            for (int i = 0; i < cliente.solicitudes[indiceSolicitud].obligadosSolidarios.length; i++) {
                if (cliente.solicitudes[indiceSolicitud].obligadosSolidarios[i].buroCredito == null && cliente.solicitudes[indiceSolicitud].obligadosSolidarios[i].circuloCredito == null) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la calificación crediticia para los obligados solidarios"));
                }
            }
            /*CERRADO EL 21/03/2013 ABRIR PARA FUTURO
            for (int i = 0; i < cliente.solicitudes[indiceSolicitud].obligadosSolidarios.length; i++) {
                if (cliente.solicitudes[indiceSolicitud].obligadosSolidarios[i].economia == null) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar los datos económicos para todos los obligados solidarios"));
                }
            }*/
        }
        if (cliente.solicitudes[indiceSolicitud].buroCredito == null && cliente.solicitudes[indiceSolicitud].circuloCredito == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la información de Calificación crediticia"));
        }
        if (cliente.solicitudes[indiceSolicitud].negocio == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Negocio cliente"));
        }
        if (cliente.solicitudes[indiceSolicitud].informacion == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sacción Información financiera"));
        }
        /*CERRADO EL 21/03/2013 ABRIR PARA FUTURO
        if ((cliente.solicitudes[indiceSolicitud].referenciasPersonales == null || cliente.solicitudes[indiceSolicitud].referenciasPersonales.length == 0) && (cliente.solicitudes[indiceSolicitud].referenciasComerciales == null || cliente.solicitudes[indiceSolicitud].referenciasPersonales.length == 0) && cliente.solicitudes[indiceSolicitud].arrendatarioDomicilio == null && cliente.solicitudes[indiceSolicitud].arrendatarioLocal == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar una referencia o un arrendatario"));
        }*/
        if (cliente.solicitudes[indiceSolicitud].montoPropuesto == 0) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Propuesta comité de crédito"));
        }
        if (cliente.solicitudes[indiceSolicitud].seguro == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Seguro de vida"));
        }

        if (notificaciones.size() != 0) {
            return false;
        }
        return true;
    }

    public static boolean clienteCompletoScore(ClienteVO cliente, int indiceSolicitud, Vector<Notification> notificaciones) {

        if (cliente.solicitudes[indiceSolicitud].buroCredito == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la información del Buró de Crédito"));
        } else {
            if (cliente.solicitudes[indiceSolicitud].buroCredito.comportamiento == 2) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El cliente cuenta con mala calificación en Buró de Crédito"));
            }
            if (cliente.solicitudes[indiceSolicitud].buroCredito.tipoCuenta == 0) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la información del Tipo de Cuenta"));
            }
            if (cliente.solicitudes[indiceSolicitud].buroCredito.antCuenta == 0) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la información de Antigüedad de la Cuenta"));
            }
            if (cliente.solicitudes[indiceSolicitud].buroCredito.numBusquedaCuenta == 0) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la información de Número de Búsquedas de la Cuenta"));
            }
        }
        if (cliente.sexo == 0) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar el Sexo del cliente"));
        }
        if (cliente.fechaNacimiento == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la Fecha de Nacimiento del cliente"));
        }
        if (cliente.solicitudes[indiceSolicitud].empleo == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Empleo"));
        } else {
            if (cliente.solicitudes[indiceSolicitud].empleo.antEmpleo == 0) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la Antigüedad en el empleo"));
            }
            if (cliente.solicitudes[indiceSolicitud].empleo.tipoContrato == 0) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar el Tipo de Contrato"));
            }

            if (cliente.solicitudes[indiceSolicitud].empleo.arraigoEmpresa == 0) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar el Arraigo de la Empresa"));
            }
            if (cliente.solicitudes[indiceSolicitud].empleo.numeroEmpleados == 0) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar el Número de empleados"));
            }
            if (cliente.solicitudes[indiceSolicitud].empleo.plazoContrato == 0) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar el Plazo de Contrato"));
            }
        }

        if (cliente.dependientesEconomicos == 0) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar los Dependientes Econímicos del cliente"));
        }

        if (cliente.solicitudes[indiceSolicitud].vivienda == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Vivienda"));
        }

        if (cliente.solicitudes[indiceSolicitud].referenciaLaboral == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Referencia Laboral"));
        } else if (cliente.solicitudes[indiceSolicitud].referenciaLaboral.jornada == 0) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar el campo Jornada"));
        }

        if (notificaciones.size() != 0) {
            return false;
        }
        return true;
    }

    public static boolean clienteCompletoConsumo(ClienteVO cliente, int indiceSolicitud, Vector<Notification> notificaciones) {

        if (cliente.nombreCompleto == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Captura abreviada"));
        }
        if (cliente.tipoIdentificacion == 0) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Datos generales"));
        }
        if (cliente.solicitudes[indiceSolicitud].scoring == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Score"));
        }
//		if ( cliente.solicitudes[indiceSolicitud].buroCredito==null ){
//			notificaciones.add( new Notification(ClientesConstants.ERROR_TYPE,"Debe capturar la secci�n Calificaci�n Crediticia") );
//		}
        if (cliente.solicitudes[indiceSolicitud].montoSolicitado == 0) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Crédito solicitado"));
        }
        if ((cliente.solicitudes[indiceSolicitud].referenciasPersonales == null || cliente.solicitudes[indiceSolicitud].referenciasPersonales.length == 0) && (cliente.solicitudes[indiceSolicitud].referenciasComerciales == null || cliente.solicitudes[indiceSolicitud].referenciasPersonales.length == 0) && cliente.solicitudes[indiceSolicitud].arrendatarioDomicilio == null && cliente.solicitudes[indiceSolicitud].arrendatarioLocal == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar una referencia o un arrendatario"));
        }
        if (cliente.solicitudes[indiceSolicitud].empleo == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Empleo"));
        }
        if (notificaciones.size() != 0) {
            return false;
        }
        return true;

    }

    public static boolean clienteCompletoDescuento(ClienteVO cliente, int indiceSolicitud, Vector<Notification> notificaciones) {

        if (cliente.nombreCompleto == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Captura abreviada"));
        }
        if (cliente.tipoIdentificacion == 0) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Datos generales"));
        }
        if (cliente.solicitudes[indiceSolicitud].scoring == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Score"));
        }
        if (cliente.solicitudes[indiceSolicitud].expediente == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe estar el expediente verificado"));
        }
        if (cliente.solicitudes[indiceSolicitud].montoSolicitado == 0) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Crédito solicitado"));
        }
        if ((cliente.solicitudes[indiceSolicitud].referenciasPersonales == null || cliente.solicitudes[indiceSolicitud].referenciasPersonales.length < 4)) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar al menos 4 referencias"));
        }
        if (cliente.solicitudes[indiceSolicitud].empleo == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Empleo"));
        }
        if (notificaciones.size() != 0) {
            return false;
        }
        return true;

    }

    public static boolean clienteCompletoAnalisisConsumo(ClienteVO cliente, int indiceSolicitud, Vector<Notification> notificaciones) {

        if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CONSUMO || cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CREDIHOGAR || cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.SELL_FINANCE) {
            if (cliente.nombreCompleto == null) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Captura abreviada"));
            }
            if (cliente.tipoIdentificacion == 0) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Datos generales"));
            }
            if (cliente.solicitudes[indiceSolicitud].scoring == null) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Score"));
            }
//			if ( cliente.solicitudes[indiceSolicitud].buroCredito==null ){
//				notificaciones.add( new Notification(ClientesConstants.ERROR_TYPE,"Debe capturar la secci�n Calificaci�n Crediticia") );
//			}
            if (cliente.solicitudes[indiceSolicitud].montoSolicitado == 0) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Crédito solicitado"));
            }
            if ((cliente.solicitudes[indiceSolicitud].referenciasPersonales == null || cliente.solicitudes[indiceSolicitud].referenciasPersonales.length == 0) && (cliente.solicitudes[indiceSolicitud].referenciasComerciales == null || cliente.solicitudes[indiceSolicitud].referenciasPersonales.length == 0) && cliente.solicitudes[indiceSolicitud].arrendatarioDomicilio == null && cliente.solicitudes[indiceSolicitud].arrendatarioLocal == null) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar una referencia o un arrendatario"));
            }
            if (cliente.solicitudes[indiceSolicitud].empleo == null) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Empleo"));
            }
            if (notificaciones.size() != 0) {
                return false;
            }
        }
        return true;

    }

    public static boolean clienteCompletoAnalisisDescuento(ClienteVO cliente, int indiceSolicitud, Vector<Notification> notificaciones) {

        if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CONSUMO || cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.CREDIHOGAR || cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.SELL_FINANCE) {
            if (cliente.nombreCompleto == null) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Captura abreviada"));
            }
            if (cliente.tipoIdentificacion == 0) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Datos generales"));
            }
            if (cliente.solicitudes[indiceSolicitud].scoring == null) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Score"));
            }
            if (cliente.solicitudes[indiceSolicitud].expediente == null) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe estar el expediente verificado"));
            }
            if (cliente.solicitudes[indiceSolicitud].montoSolicitado == 0) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Crédito solicitado"));
            }
            if ((cliente.solicitudes[indiceSolicitud].referenciasPersonales == null || cliente.solicitudes[indiceSolicitud].referenciasPersonales.length < 4)) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar al menos 4 referencias"));
            }
            if (cliente.solicitudes[indiceSolicitud].empleo == null) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Empleo"));
            }
            if (cliente.solicitudes[indiceSolicitud].scoring.coberturaPago > 1) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La cobertura de pago debe ser BUENA"));
            }
            if (notificaciones.size() != 0) {
                return false;
            }
        }
        return true;

    }

    public static boolean clienteCompletoVivienda(ClienteVO cliente, int indiceSolicitud, Vector<Notification> notificaciones) {

        if (cliente.nombreCompleto == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Captura abreviada"));
        }
        if (cliente.solicitudes[indiceSolicitud].scoring == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Score"));
        }
        if (cliente.solicitudes[indiceSolicitud].buroCredito == null) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Calificación Crediticia"));
        }
        if (cliente.solicitudes[indiceSolicitud].montoSolicitado == 0) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Crédito solicitado"));
        }

        if (notificaciones.size() != 0) {
            return false;
        }
        return true;
    }

    public static boolean tasasAplicaPlan(HttpServletRequest request, Vector<Notification> notificaciones) throws Exception {

        int planElegido = HTMLHelper.getParameterInt(request, "planSellFinance");
        int tasaElegida = HTMLHelper.getParameterInt(request, "tasa");
        int plasoElegido = HTMLHelper.getParameterInt(request, "plazoAutorizado");
        int idComicion = HTMLHelper.getParameterInt(request, "comision");

        switch (planElegido) {
            case ClientesConstants.IR100:
                if ((plasoElegido != 3 && plasoElegido != 6 && plasoElegido != 9 && plasoElegido != 12) || tasaElegida != 2 || idComicion != 1) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La combinación elegida de tasa, comisión y plazo son incorrectas para el plan seleccionado."));
                }
                break;
            case ClientesConstants.IR100_12_MAS:
                if ((plasoElegido != 18 && plasoElegido != 24 && plasoElegido != 36) || tasaElegida != 3 || idComicion != 1) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La combinación elegida de tasa, comisión y plazo son incorrectas para el plan seleccionado."));
                }
                break;
            case ClientesConstants.IR101:
                if ((plasoElegido != 6 && plasoElegido != 9 && plasoElegido != 12) || tasaElegida != 5 || idComicion != 2) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La combinación elegida de tasa, comisión y plazo son incorrectas para el plan seleccionado."));
                }
                break;
            case ClientesConstants.IR102:
                if ((plasoElegido != 6 && plasoElegido != 9 && plasoElegido != 12) || tasaElegida != 2 || idComicion != 3) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La combinación elegida de tasa, comisión y plazo son incorrectas para el plan seleccionado."));
                }
                break;
            case ClientesConstants.IR103:
                if (plasoElegido != 12 || tasaElegida != 1 || idComicion != 1) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La combinación elegida de tasa, comisión y plazo son incorrectas para el plan seleccionado."));
                }
                break;
            case ClientesConstants.IR104:
                if ((plasoElegido != 3 && plasoElegido != 6 && plasoElegido != 9 && plasoElegido != 12) || tasaElegida != 4 || idComicion != 1) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La combinación elegida de tasa, comisión y plazo son incorrectas para el plan seleccionado."));
                }
                break;
            case ClientesConstants.IR105:
                if ((plasoElegido != 3 && plasoElegido != 6 && plasoElegido != 9) || tasaElegida != 4 || idComicion != 1) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La combinación elegida de tasa, comisión y plazo son incorrectas para el plan seleccionado."));
                }
                break;
            case ClientesConstants.IR106:
                if (plasoElegido != 12 || tasaElegida != 6 || idComicion != 1) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La combinación elegida de tasa, comisión y plazo son incorrectas para el plan seleccionado."));
                }
                break;
            default:
                return false;
        }
        if (notificaciones.size() != 0) {
            return false;
        }
        return true;

    }

    public static boolean tasasAplicaPlanDescuentoNomina(HttpServletRequest request, Vector<Notification> notificaciones) throws Exception {

        int planElegido = HTMLHelper.getParameterInt(request, "planSellFinance");
        int tasaElegida = HTMLHelper.getParameterInt(request, "tasa");
        int plazoElegido = HTMLHelper.getParameterInt(request, "plazoAutorizado");
        int idComision = HTMLHelper.getParameterInt(request, "comision");
        System.out.println("plan " + planElegido);
        System.out.println("tasaElegida " + tasaElegida);
        System.out.println("plazo " + plazoElegido);
        System.out.println("comision " + idComision);

        switch (planElegido) {
            case ClientesConstants.DN_24:
                if ((plazoElegido != 24) || tasaElegida > 2 || idComision != 1) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La combinación elegida de tasa, comisión y plazo son incorrectas para el plan seleccionado."));
                }
                break;
            case ClientesConstants.DN_36:
                if ((plazoElegido != 36) || tasaElegida > 2 || idComision != 1) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La combinación elegida de tasa, comisión y plazo son incorrectas para el plan seleccionado."));
                }
                break;
            case ClientesConstants.DN_48:
                if ((plazoElegido != 48) || tasaElegida > 2 || idComision != 1) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La combinación elegida de tasa, comisión y plazo son incorrectas para el plan seleccionado."));
                }
                break;
            case ClientesConstants.DN_60:
                if ((plazoElegido != 60) || tasaElegida > 2 || idComision != 1) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La combinación elegida de tasa, comisión y plazo son incorrectas para el plan seleccionado."));
                }
                break;
            case ClientesConstants.DN_72:
                if ((plazoElegido != 72) || tasaElegida > 2 || idComision != 1) {
                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "La combinación elegida de tasa, comisión y plazo son incorrectas para el plan seleccionado."));
                }
                break;
            default:
                return false;
        }
        if (notificaciones.size() != 0) {
            return false;
        }
        return true;

    }

    public static String formatoFechaSyncronet(Date fecha) {

        Locale local = new Locale("ES");
        SimpleDateFormat sd = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_SYNCRONET, local);

        if (fecha != null) {
            return sd.format(fecha).toUpperCase();
        } else {
            return "";
        }
    }

    public static String getReferencia(String contrato) {
        String referencia = "";
        int operFinal = 0;

        if (contrato != null) {
            referencia = contrato;
            String algoritmo = "121212121212";
            int z = 0;
            contrato = FormatUtil.deleteChar(contrato, '/');
            contrato = "34" + contrato.substring(2);
            int suma = 0;
            for (int i = 11; i >= 0; i--) {
                int x = Integer.parseInt(contrato.substring(i, i + 1));
                int y = Integer.parseInt(algoritmo.substring(i, i + 1));
                z = x * y;
                if (z > 9) {
                    String oper = String.valueOf(z);
                    int res = Integer.parseInt(oper.substring(0, 1)) + Integer.parseInt(oper.substring(1, 2));
                    suma += res;
                } else {
                    suma += z;
                }
            }
            String temp = String.valueOf(suma);
            int x = Integer.parseInt(temp.substring(1));
            if (x != 0) {
                operFinal = 10 - x;
            }
            referencia += String.valueOf(operFinal);
        }

        return referencia;

    }

    public static String makeReferencia(ClienteVO cliente, int idSolicitud) {
        String referencia = "";

        int idSucursal = cliente.idSucursal;
        int idCliente = cliente.idCliente;

        referencia = FormatUtil.completaCadena(String.valueOf(idSucursal), '0', 3, "L");
        referencia += FormatUtil.completaCadena(String.valueOf(idCliente), '0', 7, "L");
        referencia += FormatUtil.completaCadena(String.valueOf(idSolicitud), '0', 2, "L");

        return getDigitoVerificador(referencia);

    }

    public static String makeReferencia(int idSucursal, int idCliente, int idSolicitud) {
        String referencia = "";

        referencia = FormatUtil.completaCadena(String.valueOf(idSucursal), '0', 3, "L");
        referencia += FormatUtil.completaCadena(String.valueOf(idCliente), '0', 7, "L");
        referencia += FormatUtil.completaCadena(String.valueOf(idSolicitud), '0', 2, "L");

        return getDigitoVerificador(referencia);

    }
    
    public static String makeReferenciaAdicional(int idSucursal, int idCliente, int idSolicitud) {
        String referencia = "";

        referencia = FormatUtil.completaCadena(String.valueOf(idSucursal), '0', 3, "L");
        referencia += "9"+FormatUtil.completaCadena(String.valueOf(idCliente), '0', 6, "L");
        referencia += FormatUtil.completaCadena(String.valueOf(idSolicitud), '0', 2, "L");

        return getDigitoVerificador(referencia);

    }

    public static String makeReferenciaDev(ClienteVO cliente, int idSolicitud) {
        String referencia = "";

        int idSucursal = cliente.idSucursal;
        int idCliente = cliente.idCliente;

        referencia = FormatUtil.completaCadena(String.valueOf(idSucursal), '0', 3, "L");
        referencia += "8" + FormatUtil.completaCadena(String.valueOf(idCliente), '0', 6, "L");
        referencia += FormatUtil.completaCadena(String.valueOf(idSolicitud), '0', 2, "L");

        return getDigitoVerificador(referencia);

    }

    public static String makeReferenciaRecargaTA(ClienteVO cliente, int idSolicitud) {
        String referencia = "";

        int idSucursal = cliente.idSucursal;
        int idCliente = cliente.idCliente;

        referencia = FormatUtil.completaCadena(String.valueOf(idSucursal), '0', 3, "L");
        referencia += FormatUtil.completaCadena(String.valueOf(idCliente), '0', 7, "L");
        referencia += FormatUtil.completaCadena(String.valueOf(idSolicitud), '0', 2, "L");

        return getDigitoVerificador(referencia);

    }

    public static String makeReferenciaGrupal(GrupoVO grupo, int idCiclo) {
        String referencia = "";
        int idSucursal = grupo.sucursal;
        int idGrupo = grupo.idGrupo;

        referencia = FormatUtil.completaCadena(String.valueOf(idSucursal), '0', 3, "L");
        referencia += "9" + FormatUtil.completaCadena(String.valueOf(idGrupo), '0', 6, "L");
        referencia += FormatUtil.completaCadena(String.valueOf(idCiclo), '0', 2, "L");

        return getDigitoVerificador(referencia);

    }

    public static String getDigitoVerificador(String referencia) {

        if (referencia != null) {
            String algoritmo = "121212121212";
            int operFinal = 0;
            int z = 0;
            int suma = 0;
            for (int i = 11; i >= 0; i--) {
                int x = Integer.parseInt(referencia.substring(i, i + 1));
                int y = Integer.parseInt(algoritmo.substring(i, i + 1));
                z = x * y;
                if (z > 9) {
                    String oper = String.valueOf(z);
                    int res = Integer.parseInt(oper.substring(0, 1)) + Integer.parseInt(oper.substring(1, 2));
                    suma += res;
                } else {
                    suma += z;
                }
            }

            int x = 0;
            String temp = String.valueOf(suma);

            if (temp.length() > 1) {
                x = Integer.parseInt(temp.substring(1));
            } else {
                x = Integer.parseInt(temp);
            }

            if (x != 0) {
                operFinal = 10 - x;
            }
            referencia += String.valueOf(operFinal);
        }

        return referencia;

    }

    public static String getDigitoVerificadorMod97(String referencia) {

        if (referencia != null) {
            int x = 0, y = 0;
            int suma = 0, conteo = 0;
            for (int i = referencia.length() - 1; i >= 0; i--) {
                conteo++;
                x = Integer.parseInt(referencia.substring(i, i + 1));
                if (conteo == 1) {
                    y = 11;
                } else if (conteo == 2) {
                    y = 13;
                } else if (conteo == 3) {
                    y = 17;
                } else if (conteo == 4) {
                    y = 19;
                } else {
                    y = 23;
                    conteo = 0;
                }
                suma += x * y;
            }
            x = suma / 97;
            y = (suma - (97 * x)) + 1;
            referencia += FormatUtil.completaCadena(y + "", '0', 2, "L");
        }
        return referencia;
    }

    public static int calculaEdad(java.sql.Date fechaNacimiento) {
        int anios = 0;
        Calendar hoy = Calendar.getInstance();
        Calendar nacimiento = Calendar.getInstance();
        nacimiento.setTimeInMillis(fechaNacimiento.getTime());
        while (nacimiento.before(hoy)) {
            nacimiento.add(Calendar.YEAR, 1);
            anios++;
        }
        return anios - 1;
    }

    public static double calculaMontoConComision(double monto, int idComision, TreeMap catalogoComisiones, int idProducto) {

        double montoFinal = 0;
        double porcentaje = 0;
        ComisionVO comision = null;

        if (idProducto != ClientesConstants.MAX_ZAPATOS) {
            return calculaMontoConComision(monto, idComision, catalogoComisiones);
        } else if (catalogoComisiones != null) {
            comision = (ComisionVO) catalogoComisiones.get(new Integer(idComision));
            porcentaje = comision.porcentaje / 100;
            montoFinal = monto / (1 - porcentaje) + ClientesConstants.CUOTA_FIJA_MAX_ZAPATOS;
            montoFinal = FormatUtil.redondeaMoneda(montoFinal);
        }

        return montoFinal;
    }

    public static double calculaMontoConComision(double monto, int idComision, TreeMap catalogoComisiones) {

        double montoFinal = 0;
        double porcentaje = 0;
        ComisionVO comision = null;

        if (catalogoComisiones != null) {
            comision = (ComisionVO) catalogoComisiones.get(new Integer(idComision));
            porcentaje = comision.porcentaje / 100;
            montoFinal = monto / (1 - porcentaje);
            montoFinal = FormatUtil.redondeaMoneda(montoFinal);
        }

        return montoFinal;
    }

    public static double calculaMontoSinComision(double monto, int idComision, TreeMap catalogoComisiones, int idProducto) {

        double montoFinal = 0;
        double porcentaje = 0;
        ComisionVO comision = null;

        if (idProducto != ClientesConstants.MAX_ZAPATOS) {
            return calculaMontoSinComision(monto, idComision, catalogoComisiones);
        } else if (catalogoComisiones != null && monto != 0) {
            comision = (ComisionVO) catalogoComisiones.get(new Integer(idComision));
            porcentaje = comision.porcentaje / 100;
            montoFinal = (monto - ClientesConstants.CUOTA_FIJA_MAX_ZAPATOS) * (1 - porcentaje);
//			montoFinal = FormatUtil.redondeaMoneda(montoFinal);
        }

        return montoFinal;
    }

    public static double calculaMontoSinComision(double monto, int idComision, TreeMap catalogoComisiones) {

        double montoFinal = 0;
        double porcentaje = 0;
        ComisionVO comision = new ComisionVO();
        if (catalogoComisiones != null && idComision != 0) {
            comision = (ComisionVO) catalogoComisiones.get(new Integer(idComision));
            porcentaje = comision.porcentaje / 100;
            montoFinal = monto * (1 - porcentaje);
//			montoFinal = FormatUtil.redondeaMoneda(montoFinal);
//			montoFinal = Math.round(montoFinal);
        } else {
            montoFinal = monto;
        }

        return montoFinal;
    }

    public static String getDomicilio(int idCliente) throws ClientesException {
        String domicilio = "";
        ClienteVO cliente = null;

        if (idCliente != 0) {
            cliente = new ClienteDAO().getCliente(idCliente);
            cliente.direcciones = new DireccionDAO().getDirecciones(idCliente);
            if (cliente.direcciones != null && cliente.direcciones.length > 0) {
                domicilio = cliente.direcciones[0].calle + " ";
                domicilio += "#" + cliente.direcciones[0].numeroExterior + " ";
                domicilio += cliente.direcciones[0].numeroInterior + " ";
                domicilio += cliente.direcciones[0].colonia + ", ";
                domicilio += cliente.direcciones[0].municipio + ", ";
                domicilio += cliente.direcciones[0].estado;
            }
        }

        return domicilio;
    }

    public static boolean clienteCompletoMaxZapatos(ClienteVO cliente, int indiceSolicitud, Vector<Notification> notificaciones) {

        if (cliente.nombreCompleto == null && cliente.direcciones != null && cliente.direcciones.length > 0) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Captura abreviada"));
        }
        if (cliente.solicitudes[indiceSolicitud].montoSolicitado == 0) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "Debe capturar la sección Crédito solicitado"));
        }
        if (notificaciones.size() != 0) {
            return false;
        }
        return true;

    }

    public static double calculaMontoMaximoGrupal(int tipoCiclo, int numCiclo, double monto, TreeMap catalogo) {

        double montoFinal = 0;
        double maximoCiclo = 0;
        if (tipoCiclo != ClientesConstants.CICLO_TIPO_REFINANCIAMIENTO || monto == 0) {
            // Si el grupo viene de otra financera se le otorga como tope el m�ximo posible JBL SEP/10
            if (tipoCiclo == ClientesConstants.CICLO_OTRA_FINANCIERA) {
                maximoCiclo = new Double(catalogo.get(7).toString());
            } else {
                maximoCiclo = new Double(catalogo.get(numCiclo).toString());
            }
            double calculos = monto + ClientesConstants.INCREMENTO_POR_CICLO;
            if (calculos <= maximoCiclo) {
                montoFinal = calculos;
            } else {
                montoFinal = maximoCiclo;
            }
        } else {
            montoFinal = monto;
        }
        return montoFinal;
    }

    public static double calculaMaxMontoIC(int idCliente) throws ClientesException {

        double montoFinal = 0;
        SolicitudVO solicitudes[] = null;
        SolicitudDAO solicituddao = new SolicitudDAO();
        ArrayList<DecisionComiteVO> decisionComite = new ArrayList<DecisionComiteVO>();
        int idSolicitud = 0;
        double montoMayor = 0;
        solicitudes = solicituddao.getSolicitudes(idCliente);
        //DecisionComiteVO decisionComite;
        for (int i = 0; i < solicitudes.length; i++) {
            if (solicitudes[i].desembolsado == 2) {
                idSolicitud = solicitudes[i].idSolicitud;
            }
        }
        if (idSolicitud == 0) {
            montoFinal = 8000;
        } else {
            decisionComite = new DecisionComiteDAO().getDecisionesComite(idCliente);
            if (decisionComite.size() > 0) {
                for (int i = 0; i < decisionComite.size(); i++) {
                    if (decisionComite.get(i).montoAutorizado > montoMayor) {
                        montoMayor = decisionComite.get(i).montoAutorizado;
                    }
                }
                if(montoMayor>8000)
                    montoFinal =montoMayor;
                else 
                    montoFinal = 8000;
            }else{
                 montoFinal = 8000;
            }

        }
        return montoFinal;
    }

    public static double calculaMontoEscalera(int idCliente, int idSolicitud) throws ClientesException {

        double montoMaximoAutorizado = 0;
        double montoFinal = 0;

        if (idSolicitud == 1) {
            montoFinal = ClientesConstants.MONTO_INICIAL;
        } else {
            montoMaximoAutorizado = new IntegranteCicloDAO().getMontoMaximoAutorizado(idCliente);
            if (idSolicitud<7)
                montoFinal = montoMaximoAutorizado + ClientesConstants.INCREMENTO_POR_CICLO;
            else{
                if(montoMaximoAutorizado >= 38000)
                    montoFinal = montoMaximoAutorizado + ClientesConstants.INCREMENTO_POR_CICLO+1000;
                else
                    montoFinal = montoMaximoAutorizado + ClientesConstants.INCREMENTO_POR_CICLO;
            }
        }
        if(montoFinal > ClientesConstants.MONTO_TOPE)
            montoFinal = ClientesConstants.MONTO_TOPE;
        return montoFinal;

    }

    public static String getNombreCliente(ClienteVO cliente, int cveNombre) {
        //1 = Devuelve el primer nombre
        //2 = Devuelve el segundo nombre

        String nombreCompleto = cliente.nombre.toUpperCase().trim();
        String primerNombre = "";
        String segundoNombre = "";
        String nombre = nombreCompleto;

        if (nombreCompleto.indexOf(" ") != -1) {
            primerNombre = cliente.nombre.substring(0, cliente.nombre.indexOf(" "));
            segundoNombre = cliente.nombre.substring(cliente.nombre.indexOf(" "));

            if (cveNombre == 1) {
                nombre = primerNombre.trim();
            } else {
                nombre = segundoNombre.trim();
            }
        }

        return nombre;
    }

    public static String getDigitoVerificadorBase19(String referencia) {

        if (referencia != null) {
            int x = 0, y = 0, suma = 0;
            for (int i = 0; i < referencia.length(); i++) {
                x = Integer.parseInt(referencia.substring(i, i + 1));
                y++;
                suma += x * y;
                if (y == 9) {
                    y = 0;
                }
            }
            referencia += String.valueOf(suma).substring(String.valueOf(suma).length() - 1, String.valueOf(suma).length());
        }

        return referencia;

    }

    public static boolean comparaDuplicadadRFC(ClienteVO clienteNueVO) throws ClientesException {
        System.out.println("Prueba de comparaDUplicidadRFC");
        boolean verificacion = false;
        ClienteDAO clienteDAO = new ClienteDAO();

        try {
            BusquedaClientesVO busquedaClientesVO = new BusquedaClientesVO();
            if ( busquedaClientesVO.nombreS == null){
                    busquedaClientesVO.nombreS = "";
                }
            if ( busquedaClientesVO.apellidoMaterno == null){
                    busquedaClientesVO.apellidoMaterno = "";
                }
            if ( busquedaClientesVO.apellidoPaterno == null){
                    busquedaClientesVO.apellidoPaterno = "";
                }
            busquedaClientesVO.RFC = clienteNueVO.rfc.substring(0,10);
            
            ClienteVO[] lstClienteVerf = clienteDAO.buscaCliente(busquedaClientesVO);
            if (lstClienteVerf.length == 0){
                    verificacion = true;
                }else {
                    for ( int i=0; i < lstClienteVerf.length; i++){
                        if (lstClienteVerf[i].getRfc().length() == 10) {
                            lstClienteVerf[i].setRfc(RFCUtil.obtenRFC(lstClienteVerf[i].getNombre(), lstClienteVerf[i].getaPaterno().equals("") ? lstClienteVerf[i].aPaterno = "X" : lstClienteVerf[i].getaPaterno(), lstClienteVerf[i].getaMaterno().equals("") ? lstClienteVerf[i].aMaterno = "X" : lstClienteVerf[i].getaMaterno(), Convertidor.dateToString(lstClienteVerf[i].getFechaNacimiento(), "yyyyMMdd")));
                            clienteDAO.updateRFC(lstClienteVerf[i].getIdCliente(), lstClienteVerf[i].getRfc());
                        }
                        if (lstClienteVerf[i].getRfc().equals(clienteNueVO.getRfc())) {
                            verificacion = false;
                            break;
                        } else {
                            if (!clienteNueVO.getNombre().equals(lstClienteVerf[i].getNombre()) || !clienteNueVO.getaMaterno().equals(lstClienteVerf[i].getaMaterno()) || !clienteNueVO.getaPaterno().equals(lstClienteVerf[i].getaPaterno())) {
                                verificacion = true;
                            }
                        }
                    }
                }   
        } catch (Exception e) {
            myLogger.error("updateRFC", e);
            throw new ClientesException(e.getMessage());
        }
        // RFCn = RFC13 El rfc nuevo tiene 13 posiciones por homoclave
        // revisar si tienes 10 posiciones RFCr (RFCr10)
        // si tiene 10 posiciones calcular homoclave y actualizar
        // RFCr10 a RFCr13
        // se comparan homoclaves de RFCn13 y RFCr13
        // si es la misma = cliente duplicado
        // si es diferente = comparacion nombre completo, fecha

        return verificacion;
        //
    }
    public static String comparaDuplicadadJUCAVI(ClienteVO clienteVo) throws ClientesException {
        ConsultaWsSoapUtil consultaWSSU = new ConsultaWsSoapUtil();
        ClienteVO clienteJUCAVI = null;
        String respuesta ="";
        try {
            clienteJUCAVI = consultaWSSU.consultaClienteIxa(clienteVo); 
            if (clienteJUCAVI==null){
                respuesta = "Hubo un error al verificar el cliente en JUCAVI favor de verificar los datos e intentar nuevamente, si el error persiste notificar a Sistemas";
            }
            else if(clienteJUCAVI.mensaje.equals("No se encontró el cliente en la base de datos.")){
                respuesta = "";
            }
            else if(clienteJUCAVI.idCliente>0){
                respuesta = "El Cliente se encuentra registrado en JUCAVI con el numero: " + clienteJUCAVI.idCliente;
            }
        } catch (Exception e) {
            myLogger.error("comparaDuplicadadJUCAVI", e);
            throw new ClientesException(e.getMessage());
        }
        return respuesta;
    }    
}

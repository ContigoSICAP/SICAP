package com.sicap.clientes.util;

import java.sql.Date;
import java.util.Calendar;

import com.sicap.clientes.dao.IncidenciaSegurosDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.SeguroHelper;
import com.sicap.clientes.vo.IncidenciaSegurosVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.PagoGrupalVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.SegurosVO;
import com.sicap.clientes.vo.SucursalVO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
/**
 * M�dulo: Gesti�n Seguros: Clase SegurosUtil
 * @author jahtechnologies
 *
 */

public class SegurosUtil {

    private static Logger myLogger = Logger.getLogger(SegurosUtil.class);
	public static int incidencias = 0; 
	
	public static void cambiaEstatusPagoSeguro(SegurosVO seg) {
		
		if(seg.saldoInsoluto == 0 || (seg.saldoActual == 0 && seg.numCuotasRestantes == 1)) {
			seg.estatus =  ClientesConstants.SEGURO_LIQUIDADO;
		}
		else { 
				if(seg.saldoActual== 0) {
					seg.estatus = ClientesConstants.SEGURO_VIGENTE;
				} 
				else if(seg.saldoActual > 0) {
					seg.estatus = ClientesConstants.SEGURO_VENCIDO;
				}
		}
		
	}
	
	public static boolean isCubreCuotaSeguro(SegurosVO seg, double saldo) {
		
		if(saldo==seg.prima)
			return true;
		return false;
		
	}
	
	public static boolean isCubreNoventaPorCiento(SegurosVO seg, double saldo) {
		double cuotaPrima = seg.prima * ClientesConstants.PORCENTAJE_APROBAR_PRIMA;
		
		if((saldo>=cuotaPrima) &&  (saldo < seg.prima)) 
			return true;
		return false;
		
	}
	public static boolean isCubreNoventaPorCiento(double prima, double saldo) {
		double cuotaPrima = prima * ClientesConstants.PORCENTAJE_APROBAR_PRIMA;
		
		if((saldo>=cuotaPrima) &&  (saldo < prima)) 
			return true;
		return false;
		
	}
		
	
	public static int getIncidencias() {
		return incidencias;
	}
	
	public static void agregarPagoSegurosIncidencias(ReferenciaGeneralVO ref, String observaciones) {
				
		IncidenciaSegurosDAO incidenciasdao = new IncidenciaSegurosDAO();
		IncidenciaSegurosVO incidencias = new IncidenciaSegurosVO();
    	incidencias.fechaMovimiento = new Date(Calendar.getInstance().getTime().getTime());
    	incidencias.idCliente = ref.numcliente;
    	incidencias.observaciones = observaciones;
    	incidencias.referencia = ref.referencia;
    	incidencias.tipoIncidencia = 1;		//preguntar que pasa con esto
    	try {
			incidenciasdao.addIncidencias(incidencias);
		} 
    	catch (ClientesException e) {			
			e.printStackTrace();
		}
	}
	    
	public static void actualizarPrimaSeguroGrupal(String numReferencia,double saldoActual) {
		
		IntegranteCicloVO[] integrantes = null;
		ReferenciaGeneralDAO referenciadao = new ReferenciaGeneralDAO();
		SegurosVO seguro = new SegurosVO();
		SegurosDAO segurodao = new SegurosDAO();
		referenciadao.getReferenciaGeneral(numReferencia);
				
		PagoGrupalVO  pagoGVO =  GrupoUtil.esPagoGrupalVO(numReferencia);
	  
		try {
			//obtenemos los integrantes del ciclo
			integrantes = new IntegranteCicloDAO().getIntegrantes(pagoGVO.numGrupo, pagoGVO.numCiclo);
				
			if(integrantes != null) {
				for(int i=0; i < integrantes.length; i++) {
					seguro = segurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idCiclo);
						
					if(seguro!=null) {	//procesa aquellos integrantes que tengan un seguro
						seguro.saldoActual=saldoActual;
						seguro.saldoInsoluto-=seguro.prima;
						cambiaEstatusPagoSeguro(seguro);
						segurodao.updateSeguro(seguro);
						
					}
												
				}
			}
		}
		catch(ClientesException e) {
			e.printStackTrace();
			myLogger.debug("CommandProcesarPagosReferenciados: No se pudo obtener los integrantes del grupo");
		}
		
	}
	
	
	public static double getPrimaSeguroGrupal(String numReferencia) {
		
		IntegranteCicloVO[] integrantes = null;
		ReferenciaGeneralDAO referenciadao = new ReferenciaGeneralDAO();
		SegurosVO seguro = new SegurosVO();
		SegurosDAO segurodao = new SegurosDAO();
		referenciadao.getReferenciaGeneral(numReferencia);
		double sumaPrimasIndividuales = 0;
		double saldoInsoluto = 0.0;
		
		PagoGrupalVO  pagoGVO =  GrupoUtil.esPagoGrupalVO(numReferencia);
	  
		try {
			//obtenemos los integrantes del ciclo
			integrantes = new IntegranteCicloDAO().getIntegrantes(pagoGVO.numGrupo, pagoGVO.numCiclo);
			if( integrantes!=null ){
				for( int i=0 ; i<integrantes.length ; i++ ){
					seguro = segurodao.getSeguros(integrantes[i].idCliente, integrantes[i].idSolicitud);
					if( seguro!=null ) {	
						//procesa aquellos integrantes que tengan un seguro
						sumaPrimasIndividuales += seguro.prima;
						saldoInsoluto += seguro.saldoInsoluto;
					}
				}
			}
		}
		catch(ClientesException e) {
			e.printStackTrace();
			myLogger.debug("CommandProcesarPagosReferenciados: No se pudo obtener los integrantes del grupo");
		}
		return ((sumaPrimasIndividuales)*(8));
	}

        /**
         * Obtiene el valor del IVA a la cantidad dada.
         * 
         * @param cantidad monto del seguro
         * @return iva
         */    
        public static double obtenerIva(double cantidad) {
            double montoSinIVA = cantidad * 100 / 116; // cantidad / 1.16
            double iva = montoSinIVA * 0.16; // cantidad - montoSinIVA
            return Math.round(iva * 100.0) / 100.0; // redondeo a dos digitos
        }
        
    /**
     * Obtiene el valor del IVA a la cantidad dada.
     *
     * @param cantidad monto del seguro
     * @return iva
     */
    public static double obtenerMontoSinIva(double cantidad) {
        double montoSinIVA = cantidad * 100 / 116; // cantidad / 1.16
        return Math.round(montoSinIVA * 100.0) / 100.0; // redondeo a dos digitos
    }
    
    /**
     * Validamos la fecha tope para que se tome el seguro financiado.
     * 
     * @param fechaSegCapturada fecha de captura del seguro
     * @return boolean
     */
    public static boolean validarFechaDeadLineSegF(java.util.Date fechaSegCapturada) {
        boolean forward = true;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // 2017-08-25
        try {
            java.util.Date fechaDeadLine = formatter.parse(SeguroConstantes.FECHA_DEAD_LINE_SEG_F);
            if (fechaSegCapturada != null && fechaSegCapturada.before(fechaDeadLine)) {
                forward = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            myLogger.debug("Ocurrio un error al validar la fecha del seguro financiado dead line");
        }
        return forward;
    }
        
    /**
     * Validacion para ver si todos los integrantes del grupo cuentan con seguro
     * financiado.
     *
     * @param integrantes integrantes del ciclo
     * @return boolean
     */
    public static boolean validarNumIntegrantesSegFinanciado(IntegranteCicloVO[] integrantes) {
        int integrantesLength = 0;
        int countIntConSegF = 0;
        if (integrantes == null) {
            return false;
        } else {
            integrantesLength = integrantes.length;
            for (int i = 0; i < integrantesLength; i++) {
                // montoConSeguroTemp esta variable almacena si el integrante contiene un monto con
                // seguro financiado
                if (integrantes[i].segContratado == SeguroConstantes.CONTRATACION_NO) {
                    countIntConSegF++;
                } else {
                    if (integrantes[i].montoConSeguroTemp > 0) {
                        countIntConSegF++;
                    }
                }
            }
        }
        myLogger.debug("Requiere ficha de pago? " + !(integrantesLength == countIntConSegF));
        return integrantesLength == countIntConSegF;
    }

    
    /**
     * JECB 26/10/2017
     * Obtiene el costo del seguro de un integrante ciclo
     * @param ic bean integrante de ciclo
     * @return importe del valor del seguro contratado por el cliente
     * @throws ClientesException 
     */
    public static double costoSeguroXIntegranteCiclo(IntegranteCicloVO ic) throws ClientesException{
        double costoSeguroCliente = 0d;
        SegurosDAO segDao = new SegurosDAO();
        SegurosVO seguroBean = segDao.getSeguros(ic.idCliente, ic.idSolicitud);
        boolean aplicaSegFin = seguroBean != null ? SegurosUtil.validarFechaDeadLineSegF(seguroBean.fechaCaptura) : false;
        myLogger.debug("costoSeguroXIntegranteCiclo aplicaSegFin:" + aplicaSegFin);
        myLogger.debug("costoSeguroXIntegranteCiclo ic.getMontoConSeguro():" + ic.getMontoConSeguro());
        myLogger.debug("costoSeguroXIntegranteCiclo ic.getMontoConSeguro():" + ic.getMontoDesembolso());
        if (aplicaSegFin && ic.segContratado == SeguroConstantes.CONTRATACION_SI && (ic.getMontoConSeguro() != 0 && ic.getMontoDesembolso() != ic.getMontoConSeguro())) {
            // Agregamos el costo del seguro Agosto 2017
            int idSucursal = ic.idSucursal;
            int tipoSeguro = ic.tipoSeguro;
            SucursalVO sucursalVo = new SucursalDAO().getSucursal(idSucursal);
            costoSeguroCliente = SeguroHelper.getCostoSeguro(tipoSeguro, sucursalVo);
        }
        return costoSeguroCliente;
    }
    
    /**
     * JECB 26/10/2017
     * @param arrayIC integrantes del ciclo del grupo
     * @return sumatoria del costo del seguro de todos los integrantes del ciclo
     * @throws ClientesException 
     */
    public static double sumatoriaSegurosXGrupo(IntegranteCicloVO[] arrayIC) throws ClientesException{
        
        double sumatoriaSegContratado = 0d; 
        SucursalDAO sucDAO = new SucursalDAO();
        
        SegurosDAO segDao = new SegurosDAO();
        SegurosVO seguroBean = null;
        
        for (int i = 0; i < arrayIC.length; i++) {
            
            seguroBean = segDao.getSeguros(arrayIC[i].idCliente, arrayIC[i].idSolicitud);
            boolean aplicaSegFin = seguroBean != null ? SegurosUtil.validarFechaDeadLineSegF(seguroBean.fechaCaptura) : false;
            
            myLogger.debug("sumatoriaSegurosXGrupo aplicaSegFin:" + aplicaSegFin);
            myLogger.debug("sumatoriaSegurosXGrupo ic.getMontoConSeguro():" + arrayIC[i].getMontoConSeguro());
            myLogger.debug("sumatoriaSegurosXGrupo ic.getMontoDesembolso():" + arrayIC[i].getMontoDesembolso());
            
            if (aplicaSegFin && arrayIC[i].segContratado == SeguroConstantes.CONTRATACION_SI && (arrayIC[i].getMontoConSeguro() != 0 && arrayIC[i].getMontoDesembolso() != arrayIC[i].getMontoConSeguro())) {
                    // Agregamos el costo del seguro Agosto 2017
                    int idSucursal = arrayIC[i].idSucursal;
                    int tipoSeguro = arrayIC[i].tipoSeguro;
                    SucursalVO sucursalVo = sucDAO.getSucursal(idSucursal);
                    sumatoriaSegContratado += SeguroHelper.getCostoSeguro(tipoSeguro, sucursalVo);
                }
        }
        return sumatoriaSegContratado;
    }
}

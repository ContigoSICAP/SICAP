package com.sicap.clientes.commands;

import java.util.Calendar;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.DisposicionDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.helpers.TablaAmortizacionHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DisposicionVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.TasaInteresVO;


public class CommandGuardaDisposiciones implements Command{


	private String siguiente;


	public CommandGuardaDisposiciones(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		SolicitudVO solicitud = new SolicitudVO();
		DisposicionVO disposiciones[] = null;
		DisposicionVO disposicion = null;
		DisposicionDAO disposiciondao = new DisposicionDAO();
		TablaAmortizacionDAO tablaAmortizaciondao = new TablaAmortizacionDAO();
		Calendar cal = Calendar.getInstance();

		try{
			double montoDisposicionConComision = 0;
			double montoDispuesto = 0;
			TasaInteresVO tasa = null;
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			int idDisposicion = HTMLHelper.getParameterInt(request, "idDisposicion");
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			solicitud = cliente.solicitudes[indiceSolicitud];
			TreeMap catComisiones = CatalogoHelper.getCatalogoComisiones(solicitud.tipoOperacion);
			TreeMap catTasas = CatalogoHelper.getCatalogoTasas(solicitud.tipoOperacion);
			disposiciones = solicitud.disposiciones;
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaDisposiciones");
			if ( idDisposicion==0 ){
				disposicion = SolicitudHelper.getDisposicionVO(new DisposicionVO(), request);
				if ( solicitud.estatus==ClientesConstants.SOLICITUD_AUTORIZADA && solicitud.decisionComite!=null ){
					int numDisposiciones = 0;
					if ( disposiciones!=null ){
						numDisposiciones = disposiciones.length;
						for ( int i=0 ; i<disposiciones.length ; i++ ) montoDispuesto += disposiciones[i].monto;
					}
					double monto = solicitud.decisionComite.montoSinComision;
					if ( monto==0 )
						monto = ClientesUtil.calculaMontoSinComision(solicitud.decisionComite.montoAutorizado, solicitud.decisionComite.comision, catComisiones, solicitud.tipoOperacion);
					if ( disposicion.monto<=monto-montoDispuesto ){
						disposiciondao.addDisposicion(cliente.idCliente, solicitud.idSolicitud, disposicion);
						montoDisposicionConComision = ClientesUtil.calculaMontoConComision(disposicion.monto, solicitud.decisionComite.comision, catComisiones, solicitud.tipoOperacion);
						tasa = (TasaInteresVO)catTasas.get(solicitud.decisionComite.tasa);
						TablaAmortizacionHelper.insertTablaMaxZapatos(solicitud.idCliente, solicitud.idSolicitud, disposicion.idDisposicion, cliente.idSucursal , montoDisposicionConComision, disposicion.monto, 2, solicitud.decisionComite.frecuenciaPago, tasa.valor, cal.getTime());
						disposicion.tablaAmostizaciones = tablaAmortizaciondao.getElementos(cliente.idCliente, solicitud.idSolicitud, disposicion.idDisposicion, ClientesConstants.AMORTIZACION_INDIVIDUAL);
						notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
						
						DisposicionVO disposicionesActuales[] = new DisposicionVO[numDisposiciones+1];
						for( int i = 0 ; i<numDisposiciones ; i++ ){
							disposicionesActuales[i] = disposiciones[i];
						}
						disposicionesActuales[disposicion.idDisposicion-1] = disposicion;
						disposiciones = disposicionesActuales;
					}else
						notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "La disposición excede el monto de su línea de crédito");
				}else{
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Para capturar una disposición es necesario que el crédito se encuentre autorizado");
				}
			}else{
				disposicion = SolicitudHelper.getDisposicionVO(disposiciones[idDisposicion-1], request);
				if ( solicitud.estatus==ClientesConstants.SOLICITUD_AUTORIZADA ){
					double monto = solicitud.decisionComite.montoSinComision;
					if ( monto==0 )
						monto = ClientesUtil.calculaMontoSinComision(solicitud.decisionComite.montoAutorizado, solicitud.decisionComite.comision, catComisiones, solicitud.tipoOperacion);
					Logger.debug("monto : "+monto);
					Logger.debug("disposiciones[0].monto : "+disposicion.monto);
					if ( disposicion.monto<=monto ){
						disposiciondao.updateDisposicion(cliente.idCliente, solicitud.idSolicitud, idDisposicion, disposicion);
						montoDisposicionConComision = ClientesUtil.calculaMontoConComision(disposicion.monto, solicitud.decisionComite.comision, catComisiones, solicitud.tipoOperacion);
						tasa = (TasaInteresVO)catTasas.get(solicitud.decisionComite.tasa);
                                                System.out.println("_______________________________"+request.getRemoteUser()+" CommandGuardaDisposiciones"+" solicitud.idCliente "+solicitud.idCliente+" solicitud.idSolicitud "+solicitud.idSolicitud);
						tablaAmortizaciondao.delTablaAmortizacion(solicitud.idCliente, solicitud.idSolicitud, disposicion.idDisposicion, ClientesConstants.AMORTIZACION_INDIVIDUAL);
						TablaAmortizacionHelper.insertTablaMaxZapatos(solicitud.idCliente, solicitud.idSolicitud, disposicion.idDisposicion, cliente.idSucursal , montoDisposicionConComision, disposicion.monto, 2, solicitud.decisionComite.frecuenciaPago, tasa.valor, cal.getTime());
						disposicion.tablaAmostizaciones = tablaAmortizaciondao.getElementos(disposicion.idCliente, disposicion.idSolicitud, disposicion.idDisposicion, ClientesConstants.AMORTIZACION_INDIVIDUAL);
						notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron actualizados correctamente");
					}else
						notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El monto debe ser menor o igual al autorizado");
				}
				disposiciones[idDisposicion-1] = disposicion;
			}
			bitutil.registraEvento(disposicion);
			request.setAttribute("NOTIFICACIONES",notificaciones);
			request.setAttribute("DISPOSICION", disposicion);
			cliente.solicitudes[indiceSolicitud].disposiciones = disposiciones;
			//Actualiza el cliente en sesion
			session.setAttribute("CLIENTE", cliente);
		}catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		return siguiente;
	}


}
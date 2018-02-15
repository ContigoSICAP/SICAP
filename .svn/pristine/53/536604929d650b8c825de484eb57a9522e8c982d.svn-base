package com.sicap.clientes.commands;

import java.util.Calendar;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ObligadoSolidarioDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.dao.TelefonoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.TablaAmortizacionHelper;
import com.sicap.clientes.helpers.ibs.CreditoHelperIBS;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CatalogoVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ComisionVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.TasaInteresVO;

public class CommandRegistroMasivoIBS implements Command{


	private String siguiente;


	public CommandRegistroMasivoIBS(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		DireccionDAO direcciondao = new DireccionDAO();
		TelefonoDAO telefonodao = new TelefonoDAO();
		ObligadoSolidarioDAO obligadodao = new ObligadoSolidarioDAO();
		CatalogoVO localidad = null;
		CatalogoDAO dao = new CatalogoDAO();
		TablaAmortizacionDAO tablaDao = new TablaAmortizacionDAO();
		Calendar cal = Calendar.getInstance();
		Vector<Notification> notificaciones = new Vector<Notification>();
		SolicitudDAO solicituddao = new SolicitudDAO();
		CicloGrupalDAO ciclodao = new CicloGrupalDAO();

		try{
			String fecha = HTMLHelper.getParameterString(request, "fecha");
			TreeMap<Integer,ComisionVO> catComisionesMicro = CatalogoHelper.getCatalogoComisiones( ClientesConstants.MICROCREDITO );
			TreeMap<Integer,ComisionVO> catComisionesConsumo = CatalogoHelper.getCatalogoComisiones( ClientesConstants.CONSUMO );

			//Barre todos los créditos individuales
			SolicitudVO[] solicitudes = new SolicitudDAO().getSolicitudesForIBS( Convertidor.stringToSqlDate(fecha) );
			for ( int i=0; i < solicitudes.length; i++ ){
				CreditoHelperIBS creditoHelper = new CreditoHelperIBS();

				ClienteVO cliente = new ClienteDAO().getCliente( solicitudes[i].idCliente );
				cliente.idBanco = new SucursalDAO().getSucursal( cliente.idSucursal ).idBanco;
				cliente.direcciones = direcciondao.getDirecciones( cliente.idCliente );
				
				if ( cliente.direcciones!=null ){
					localidad = dao.getLocalidad( cliente.direcciones[0].idColonia, cliente.direcciones[0].idLocalidad );
					if (localidad != null)
						cliente.direcciones[0].localidad = localidad.descripcion;
				}
				
				for ( int h=0 ; cliente.direcciones!=null && h<cliente.direcciones.length ; h++ ){
					cliente.direcciones[h].telefonos = telefonodao.getTelefonos( cliente.idCliente, cliente.direcciones[h].idDireccion );
				}

				solicitudes[i].seguro = new SegurosDAO().getSeguros( cliente.idCliente, solicitudes[i].idSolicitud );
				solicitudes[i].obligadosSolidarios = obligadodao.getObligadosSolidarios( cliente.idCliente, solicitudes[i].idSolicitud );
				cliente.idBanco = new SucursalDAO().getSucursal( cliente.idSucursal ).idBanco;
				solicitudes[i].referencia = new ReferenciaGeneralDAO().getReferencia( cliente.idCliente, solicitudes[i].idSolicitud, 'I');
				solicitudes[i].decisionComite = new DecisionComiteDAO().getDecisionComite( cliente.idCliente, solicitudes[i].idSolicitud );
				if ( solicitudes[i].tipoOperacion == ClientesConstants.CONSUMO )
					solicitudes[i].decisionComite.montoSinComision = ClientesUtil.calculaMontoSinComision(solicitudes[i].decisionComite.montoAutorizado, solicitudes[i].decisionComite.comision, catComisionesConsumo);
				else if ( solicitudes[i].tipoOperacion == ClientesConstants.MICROCREDITO )
					solicitudes[i].decisionComite.montoSinComision = ClientesUtil.calculaMontoSinComision(solicitudes[i].decisionComite.montoAutorizado, solicitudes[i].decisionComite.comision, catComisionesMicro);
				System.out.println("_______________________________"+request.getRemoteUser()+" CommandRegistroMasivoIBS"+" solicitud.idCliente "+cliente.idCliente+" solicitud.idSolicitud "+solicitudes[i].idSolicitud);
                                tablaDao.delTablaAmortizacion( cliente.idCliente, solicitudes[i].idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL );

				TreeMap catTasas = CatalogoHelper.getCatalogoTasas(solicitudes[i].tipoOperacion);
				TasaInteresVO tasa = (TasaInteresVO)catTasas.get(solicitudes[i].decisionComite.tasa);
				//Genera la tabla de amortización
				Double pagoUnitario = TablaAmortizacionHelper.calcPagoUnitario( tasa.valor, solicitudes[i].decisionComite.montoAutorizado, solicitudes[i].decisionComite.plazoAutorizado, solicitudes[i].decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()),cliente.idSucursal, solicitudes[i].tipoOperacion );
				Double tasaLogaritmo = 	TablaAmortizacionHelper.getTasaLogaritmico ( solicitudes[i].decisionComite.montoAutorizado, pagoUnitario, solicitudes[i].decisionComite.plazoAutorizado, solicitudes[i].decisionComite.frecuenciaPago, 0.00);
				Double tasaCalculada = TablaAmortizacionHelper.calcTasa( solicitudes[i].tipoOperacion, solicitudes[i].decisionComite.montoAutorizado, pagoUnitario, solicitudes[i].decisionComite.plazoAutorizado, solicitudes[i].decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), cliente.idSucursal, tasaLogaritmo );
				solicitudes[i].cuota = pagoUnitario;
				solicitudes[i].tasaCalculada = tasaCalculada;

				if ( solicitudes[i].tipoOperacion == ClientesConstants.CONSUMO )
					TablaAmortizacionHelper.insertTablaInsolutoConsumo( cliente, solicitudes[i], solicitudes[i].decisionComite.montoAutorizado, solicitudes[i].decisionComite.montoSinComision, pagoUnitario, solicitudes[i].decisionComite.plazoAutorizado, solicitudes[i].decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), tasaCalculada );
				else if ( solicitudes[i].tipoOperacion == ClientesConstants.MICROCREDITO )
					TablaAmortizacionHelper.insertTablaInsolutoMicro( cliente, solicitudes[i], solicitudes[i].decisionComite.montoAutorizado, solicitudes[i].decisionComite.montoSinComision, pagoUnitario, solicitudes[i].decisionComite.plazoAutorizado, solicitudes[i].decisionComite.frecuenciaPago, Convertidor.dateToString(cal.getTime()), tasaCalculada );

				solicitudes[i].amortizacion = new TablaAmortizacionDAO().getElementos( cliente.idCliente, solicitudes[i].idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL );
				creditoHelper.registraCreditoIBS( cliente, solicitudes[i], request, notificaciones );
				solicituddao.updateSolicitud( cliente.idCliente, solicitudes[i] );
			}
			
			//Barre los créditos Grupales
			CicloGrupalVO[] ciclos = new CicloGrupalDAO().getCiclosForIBS( Convertidor.stringToSqlDate(fecha) );
			for (  int i=0; i < ciclos.length ; i++ ){
				GrupoVO grupo = new GrupoDAO().getGrupo( ciclos[i].idGrupo );
				CicloGrupalVO ciclo = new CicloGrupalDAO().getCiclo( ciclos[i].idGrupo, ciclos[i].idCiclo );
				ciclo.integrantes = new IntegranteCicloDAO().getIntegrantes(ciclos[i].idGrupo, ciclos[i].idCiclo);
                                System.out.println("_______________________________"+request.getRemoteUser()+" CommandGuardaRenovacionCicloGrupal"+" ciclo.idGrupo "+ciclo.idGrupo+" ciclo.idCiclo "+ciclo.idCiclo);
				GrupoHelper.obtenTablaAmortizacion( grupo, ciclo, Convertidor.stringToDate(fecha) );
				//GrupoHelper.registerIBS( grupo, ciclo, request, notificacionees );
				ciclodao.updateCicloCredito(ciclo);
			}
			
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

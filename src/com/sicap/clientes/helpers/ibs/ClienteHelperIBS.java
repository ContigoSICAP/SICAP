package com.sicap.clientes.helpers.ibs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.TelefonoDAO;
import com.sicap.clientes.dao.ibs.ClienteIBSDAO;
import com.sicap.clientes.helpers.ClienteHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.util.inffinix.InffinixUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.TelefonoVO;
import com.sicap.clientes.vo.ibs.ClienteIBSVO;

public class ClienteHelperIBS{

	public ClienteIBSVO registraClienteCuentaIBS( ClienteVO cliente, SolicitudVO solicitud, HttpServletRequest request, boolean clienteNuevo , Vector notificaciones) throws Exception {

		int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, solicitud.idSolicitud);
		HttpSession session = request.getSession();
		ClienteIBSVO objlClient = new ClienteIBSVO();
		loadSources();

		/*else{
			objlClient.setClienteID(String.valueOf(cliente.idClienteIBS));
			System.out.println("NUMERO CLIENTE: " + String.valueOf(cliente.idClienteIBS));
			objlClient.setNumeroCuenta(String.valueOf(solicitud.idCuentaIBS));
			System.out.println("NUMERO CUENTA: " + String.valueOf(solicitud.idCuentaIBS));
		}*/
	
		try{
			if ( clienteNuevo ){
				objlClient = getClientInfo(cliente, solicitud, request.getRemoteUser());
				ClienteIBSDAO.getInstance().saveClient(objlClient, "S47829PETL");
				cliente.idClienteIBS = Convertidor.stringToInt(objlClient.getClienteID());
				solicitud.idCuentaIBS = Convertidor.stringToInt(objlClient.getNumeroCuenta());	
			}else{
				objlClient.setClienteID(String.valueOf(cliente.idClienteIBS));
				ClienteIBSDAO.getInstance().saveClient(objlClient, "S47829PETL");
				solicitud.idCuentaIBS = Convertidor.stringToInt(objlClient.getNumeroCuenta());
			}

			if ( cliente.idClienteIBS != 0 && solicitud.idCuentaIBS != 0 ){
				new ClienteDAO().updateClienteIBS(cliente.idClienteIBS, cliente.idCliente );
				new SolicitudDAO().updateSolicitudCredito(solicitud);
				cliente.solicitudes[indiceSolicitud] = solicitud;
				session.setAttribute("CLIENTE", cliente);
			}

		}catch(Exception e){
			notificaciones.add( new Notification(ClientesConstants.ERROR_TYPE ,"No se logró registrar el CLIENTE y/o CUENTA en el sistema de cartera IBS -- " + e.getMessage()) );
			e.printStackTrace();
		}
		
		return objlClient;
	}

	public ClienteIBSVO registraGrupoCuentaIBS( GrupoVO grupo, CicloGrupalVO ciclo, HttpServletRequest request, boolean grupoNuevo , Vector notificaciones ) throws Exception {

		HttpSession session = request.getSession();
		ClienteIBSVO objlClient = new ClienteIBSVO();
		loadSources();

	
		try{
			if ( grupoNuevo ){
				objlClient = getGrupoInfo(grupo, ciclo, request.getRemoteUser());
				ClienteIBSDAO.getInstance().saveClient(objlClient, "S47829PETL");
				grupo.idGrupoIBS = Convertidor.stringToInt(objlClient.getClienteID());
				ciclo.idCuentaIBS = Convertidor.stringToInt(objlClient.getNumeroCuenta());
			}else{
				objlClient.setClienteID(String.valueOf(grupo.idGrupoIBS));
				ClienteIBSDAO.getInstance().saveClient(objlClient, "S47829PETL");
				ciclo.idCuentaIBS = Convertidor.stringToInt(objlClient.getNumeroCuenta());
			}

			if ( grupo.idGrupo != 0 && ciclo.idCuentaIBS != 0 ){
				new GrupoDAO().updateGrupoIBS(grupo.idGrupoIBS, grupo.idGrupo );
				new CicloGrupalDAO().updateCicloCredito(ciclo);
				grupo.ciclos[ciclo.idCiclo-1] = ciclo;
				session.setAttribute("GRUPO", grupo);
			}

		}catch(Exception e){
			//notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE ,"No se logr� registrar el GRUPO y/o CUENTA en el sistema de cartera IBS -- " + e.getMessage()) ;
			notificaciones.add( new Notification(ClientesConstants.ERROR_TYPE ,"No se logró registrar el GRUPO y/o CUENTA en el sistema de cartera IBS -- " + e.getMessage()) );
			e.printStackTrace();
		}
		
		return objlClient;
	}

	private void loadSources() {
		InputStream objlInputStream = null;

		objlInputStream =
			this.getClass().getClassLoader().getResourceAsStream("com/afirme/clientes/resources/sources.xml");
		try {
			objlInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static ClienteIBSVO getClientInfo( ClienteVO cliente, SolicitudVO solicitud, String user ) {
		Calendar cal = Calendar.getInstance();
		ClienteIBSVO objlClient = new ClienteIBSVO();
		DireccionVO dir = null;
		String calle = "";

		String sexo = "";
		if ( cliente.sexo == 1 ) sexo = "F"; else sexo = "M";
		
		try{
			if ( cliente.direcciones != null )
				dir = cliente.direcciones[0];

			TelefonoVO telefonoPrincipal = ClienteHelper.getTelefono(dir.telefonos, ClientesConstants.TELEFONO_PRINCIPAL);
			objlClient.setNumeroClienteSicap( String.valueOf(cliente.idCliente) );
			objlClient.setApellidoPaterno( cliente.aPaterno.toUpperCase() );
			objlClient.setApellidoMaterno( cliente.aMaterno.toUpperCase() );
			objlClient.setPrimerNombre( cliente.nombre );
			objlClient.setMesNacimiento( FechasUtil.obtenParteFecha( cliente.fechaNacimiento, 2 ) );
			objlClient.setDiaNacimiento( FechasUtil.obtenParteFecha( cliente.fechaNacimiento, 1 ) );
			objlClient.setAnioNacimiento( FechasUtil.obtenParteFecha( cliente.fechaNacimiento, 3 ));
			calle = dir.calle + " " + dir.numeroExterior + " " + dir.numeroInterior;
			if ( calle.length() > 35)
				calle = calle.substring( 0, 34 );
			objlClient.setCalleNumero( calle );
			objlClient.setColonia( InffinixUtil.validaCadena(dir.colonia, "NO APLICA", 35, 0, 35, true) );
			objlClient.setCodigoCiudad( FormatUtil.completaCadena(String.valueOf(dir.numMunicipio) , '0', 3, "L"));
			objlClient.setCodigoPais("1");
			objlClient.setCodigoEstado( String.valueOf(dir.numestado) );
			objlClient.setCodigoColonia( String.valueOf(dir.idColonia) );
			objlClient.setCiudad( dir.municipio );
			objlClient.setEstado( getEDO(dir.numestado) );
			objlClient.setPais("MEXICO");
			objlClient.setSexo( sexo );
			objlClient.setEstadoCivil( String.valueOf(cliente.estadoCivil) );
			objlClient.setRfc( cliente.rfc );
			objlClient.setTelefono( telefonoPrincipal.numeroTelefono );
			objlClient.setCodigoPostal( dir.cp );
			objlClient.setCurp( cliente.curp );
			objlClient.setStatusCliente("1");
			objlClient.setUsuarioSicap( user );
			objlClient.setFechaAlta( Convertidor.formatDateCirculo(Convertidor.dateToString(cal.getTime())) );
		}catch(Exception e){
			e.printStackTrace();
		}
		return objlClient;
	}

	private static ClienteIBSVO getGrupoInfo( GrupoVO grupo, CicloGrupalVO ciclo, String user ) {
		Calendar cal = Calendar.getInstance();
		ClienteIBSVO objlClient = new ClienteIBSVO();
		IntegranteCicloVO integrante = new IntegranteCicloVO();
		ClienteVO cliente = new ClienteVO();
		ClienteDAO clientedao = new ClienteDAO();
		DireccionVO dir = null;
		String calle = "";

		String sexo = "M";
		
		try{
			if ( ciclo.integrantes != null ){
				for ( int i=0; i<ciclo.integrantes.length; i++ ){
					integrante = ciclo.integrantes[i];
					if ( integrante.rol == ClientesConstants.ROL_PRESIDENTE )
						break;
				}
			}
			
			cliente = clientedao.getCliente(integrante.idCliente);
			cliente.direcciones = new DireccionDAO().getDirecciones(integrante.idCliente);
			if ( cliente.direcciones != null ){
				dir = new DireccionDAO().getDireccion(cliente.direcciones[0].idCliente,cliente.direcciones[0].idSolicitud,cliente.direcciones[0].tabla, cliente.direcciones[0].indiceTabla);
				dir.telefonos = new TelefonoDAO().getTelefonos(dir.idCliente, dir.idDireccion);
			}
			
			TelefonoVO telefonoPrincipal = ClienteHelper.getTelefono(dir.telefonos, ClientesConstants.TELEFONO_PRINCIPAL);
			objlClient.setNumeroClienteSicap( String.valueOf(grupo.idGrupo) );
			objlClient.setApellidoPaterno( "." );
			objlClient.setApellidoMaterno( "." );
			objlClient.setPrimerNombre( grupo.nombre );
			objlClient.setMesNacimiento( FechasUtil.obtenParteFecha( grupo.fechaFormacion, 2 ) );
			objlClient.setDiaNacimiento( FechasUtil.obtenParteFecha( grupo.fechaFormacion, 1 ) );
			objlClient.setAnioNacimiento( FechasUtil.obtenParteFecha( grupo.fechaFormacion, 3 ));
			calle = dir.calle + " " + dir.numeroExterior + " " + dir.numeroInterior;
			if ( calle.length() > 35)
				calle = calle.substring( 0, 34 );
			objlClient.setCalleNumero( calle );
			objlClient.setColonia( dir.colonia );
			objlClient.setCodigoCiudad( FormatUtil.completaCadena(String.valueOf(dir.numMunicipio) , '0', 3, "L"));
			objlClient.setCodigoPais("1");
			objlClient.setCodigoEstado( String.valueOf(dir.numestado) );
			objlClient.setCodigoColonia( String.valueOf(dir.idColonia) );
			objlClient.setCiudad( dir.municipio );
			objlClient.setEstado( getEDO(dir.numestado) );
			objlClient.setPais("MEXICO");
			objlClient.setSexo( sexo );
			objlClient.setEstadoCivil( String.valueOf(cliente.estadoCivil) );
			objlClient.setRfc( cliente.rfc );
			objlClient.setTelefono( telefonoPrincipal.numeroTelefono );
			objlClient.setCodigoPostal( dir.cp );
			objlClient.setCurp( cliente.curp );
			objlClient.setStatusCliente("1");
			objlClient.setUsuarioSicap( user );
			objlClient.setFechaAlta( Convertidor.formatDateCirculo(Convertidor.dateToString(cal.getTime())) );
		}catch(Exception e){
			e.printStackTrace();
		}
		return objlClient;
	}

	//Obtiene abreviatura de estados para IBS
	private static String getEDO(int cveEstado){
		
		Hashtable<String, String> estado = new Hashtable<String, String>();
		estado.put("1", "AGS");
		estado.put("2", "BCN");
		estado.put("3", "BCS");
		estado.put("4", "CAM");
		estado.put("5", "COA");
		estado.put("6", "COL");
		estado.put("7", "CHS");
		estado.put("8", "CHI");
		estado.put("9", "DF");
		estado.put("10", "DGO");
		estado.put("11", "GTO");
		estado.put("12", "GRO");
		estado.put("13", "HGO");
		estado.put("14", "JAL");
		estado.put("15", "MX");
		estado.put("16", "MIC");
		estado.put("17", "MOR");
		estado.put("18", "NAY");
		estado.put("19", "NL");
		estado.put("20", "OAX");
		estado.put("21", "PUE");
		estado.put("22", "QRO");
		estado.put("23", "QR");
		estado.put("24", "SLP");
		estado.put("25", "SIN");
		estado.put("26", "SON");
		estado.put("27", "TAB");
		estado.put("28", "TAM");
		estado.put("29", "TLA");
		estado.put("30", "VER");
		estado.put("31", "YUC");
		estado.put("32", "ZAC");
		
		String resp = (String)estado.get(cveEstado);

		return resp;
	}

}
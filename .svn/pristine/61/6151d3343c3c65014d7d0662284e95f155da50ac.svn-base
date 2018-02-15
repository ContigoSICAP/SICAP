<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%><%@ page import="java.util.*"%><%@ page import="com.sicap.clientes.vo.*"%><%@ page import="com.sicap.clientes.dao.*"%><%@ page import="com.sicap.clientes.util.*"%><%@ page import="com.sicap.clientes.helpers.*"%><%@ page import="java.text.NumberFormat"%><jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" /><%
	ArrayList array = new ArrayList();
	mySmartUpload.initialize(pageContext);
	mySmartUpload.upload();
	NumberFormat NF = NumberFormat.getInstance();
	NF.setMaximumFractionDigits(2);

	StringBuffer cargaCirculo = new StringBuffer();
	String periodoReporte = request.getParameter("periodo");
	Hashtable noMatch = new Hashtable();
	int cont = 0;
	int totalesEmpleo = 0;
	int totalesNombre = 0;
	int totalesDireccion = 0;
	int totalesCuenta = 0;
	int numGrupos = 0;
	int numClientes = 0;
	
	StringBuffer incidenciaSaldoActual = new StringBuffer();
	StringBuffer incidenciaMensualidad = new StringBuffer();
	StringBuffer incidenciaSaldoVencido = new StringBuffer();
	int incSaldo = 0;
	int incMensualidad = 0;
	int incVencido = 0;
	Double montoCredito = null;
	Double saldoActual = null;
//	Long saldoTotales = new Long(0);
	Double saldoTotales = null;
//	Long vencidosTotales = new Long(0);
	Double vencidosTotales = null;
	Double mensualidad = null;
	Double saldoVencido = null;
	String fechaDesembolso = null;
	String fechaUltPago = "";
	String frecuencia = null;
	String RFC = "";
	Integer plazo = 0;
	Integer diasMora = 0;					
	int contadorMorosos = 0;
	int registrosProcesados = 0;
	int cuentasCerradas = 0;
	Date fechaInicioReferencias = Convertidor.stringToDate(ClientesConstants.FECHA_INICIO_CUENTAS_BURO);	
	//Calendar cal = Calendar.getInstance();
	//String fechaReporte = String.valueOf(cal.get(cal.YEAR)) + String.valueOf(cal.get(cal.MONTH+1)) + String.valueOf(cal.get(cal.DAY_OF_MONTH));
	TreeMap catalogo = MapeoHelper.getCatalogoMapeo(1);
	SaldoT24DAO cartera = new SaldoT24DAO();
	SaldoT24VO[] clientes = null;
	
	
	cargaCirculo.append("<?xml version='1.0' encoding = 'ISO-8859-1'?>\r");
	cargaCirculo.append("<Carga xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='/Carga.xsd'>\r");
	cargaCirculo.append("<Encabezado>\r");
	cargaCirculo.append("<ClaveOtorgante>0003200045</ClaveOtorgante>\r");
	cargaCirculo.append("<NombreOtorgante>FINAFIRM</NombreOtorgante>\r");
	cargaCirculo.append("<IdentificadorDeMedio>1</IdentificadorDeMedio>\r");
	cargaCirculo.append("<FechaExtraccion>"+ periodoReporte +"</FechaExtraccion>\r");
	cargaCirculo.append("<NotaOtorgante></NotaOtorgante>\r");
	cargaCirculo.append("</Encabezado>\r");
	cargaCirculo.append("<Personas>\r");
	
	try{
		for ( int paso=1; paso<5; paso++){
			//System.out.println("Paso 1");
			clientes = cartera.getVSaldosT24( paso );
			
			for( int j=0; j<clientes.length; j++ ){
				//StringTokenizer st = new StringTokenizer(FormatUtil.deleteChar(lines[j].trim(), '.'),",");
				//System.out.println("Paso 2");
				if ( clientes[j].situacionActualCredito != 4 ){
					//System.out.println("Paso 3");
					//System.out.println("Registros totales: " + registrosTotales );
					//registrosProcesados++;
					Date pagoMax = new Date();
					//RFC = clientes[j].rfc;
					montoCredito = clientes[j].montoLineaCredito;
					mensualidad = clientes[j].montoAmortizacion + clientes[j].montoInteresesPorCobrar;
					saldoVencido = clientes[j].capitalVencido;
					saldoActual = clientes[j].saldoInsoluto;
					fechaDesembolso = Convertidor.dateToString(clientes[j].fechaDisposicion);
					pagoMax = new PagoDAO().getUltimoPago(clientes[j].noCuenta);
					Integer pagVencidos = 0;
					
					String MOP = " V";
					
					if ( pagoMax != null )
						fechaUltPago = Convertidor.dateToString(pagoMax);
					else
						fechaUltPago = fechaDesembolso;
					frecuencia = clientes[j].frecuenciaAmortizacion;
					plazo = clientes[j].numCuotas;
					
					//Verifica si el cliente está en la tabla de clientes morosos
					//ClienteMorosoVO clienteMoroso = new ClienteMorosoVO();
					//clienteMoroso = new ClienteMorosoDAO().getClienteMoroso(clientes[j].noCuenta);
					
					//if ( clienteMoroso != null ){
					//	diasMora = clientes[j].diasVencidos;
					//	pagVencidos = clientes[j].numeroPagosVencidos;
					//}else{
					//	diasMora = 0;
					//}
					
					//if ( saldoActual == 0 )
					//	diasMora = 0;
					//else

					diasMora = clientes[j].diasVencidos;
					pagVencidos = clientes[j].numeroPagosVencidos;
		
					if ( mensualidad == 0 && saldoActual > 0 && clientes[j].situacionActualCredito == 2 )
						mensualidad = clientes[j].totalExigible;
					if ( saldoActual <= 0 )
						diasMora = 0;
					//if ( mensualidad == 0 && saldoActual > 0 && clientes[j].situacionActualCredito == 3 )
					//	saldoActual = 0.00;
		
					if ( saldoVencido == 0 )
						diasMora = 0;
					if ( clientes[j].situacionActualCredito == 3 ){
						saldoActual = 0.00;
						diasMora = 0;
					}
					frecuencia="M";
					
					if ( diasMora > 0 ){
						MOP = FormatUtil.completaCadena(String.valueOf(pagVencidos),'0', 2, "L");
					}
		
					ClienteDAO clienteDAO = new ClienteDAO();
					DireccionDAO direccionDAO = new DireccionDAO();
					ClienteVO infoCliente = new ClienteVO();
					DireccionVO[] infoDireccion = null;
					DireccionVO dir = null;
					
					if( saldoActual == 0 && !MOP.equals(" V") ){
						incidenciaSaldoActual.append(RFC + "\n");
						incSaldo++;
						Logger.debug("Incidencia en RFC: " + RFC + " -- " + "SaldoActual vs MOP: " + saldoActual);
					}
					
					if( mensualidad == 0 && saldoActual > 0 ){
						incidenciaMensualidad.append(RFC + "\n");
						incMensualidad++;
						Logger.debug("Incidencia en RFC: " + RFC + " -- " + "SaldoActual vs Mensualidad: " + saldoActual);
					}
					
					if( saldoVencido == 0 && !MOP.equals(" V") ){
						incidenciaSaldoVencido.append(RFC + "\n");
						incVencido++;
						Logger.debug("Incidencia en RFC: " + RFC + " -- " + "SaldoVencido: vs MOP" + saldoVencido);
					}
					
					if ( clientes[j].numOperacion == ClientesConstants.GRUPAL ){
						//System.out.println("Paso 4");
						fechaDesembolso = Convertidor.formatDateCirculo(fechaDesembolso);
						fechaUltPago = Convertidor.formatDateCirculo(fechaUltPago);
						numGrupos++;
						IntegranteCicloVO[] integrantes = new IntegranteCicloDAO().getIntegrantes( clientes[j].numCliente, clientes[j].ciclo );
						if ( integrantes!= null ){
							for ( int y=0; y<integrantes.length; y++ ){
								infoCliente = new ClienteDAO().getCliente( integrantes[y].idCliente );
								//System.out.println("Paso 5");
								if ( infoCliente != null && infoCliente.fechaNacimiento != null ){
									String referencia = ClientesUtil.makeReferencia( infoCliente, integrantes[y].idSolicitud );
									registrosProcesados++;
									//fechaDesembolso = FormatUtil.convierteFecha(fechaDesembolso,0);
									//fechaDesembolso = Convertidor.formatDateCirculo(fechaDesembolso);
									//fechaUltPago = FormatUtil.convierteFecha(fechaUltPago,0);
									//fechaUltPago = Convertidor.formatDateCirculo(fechaUltPago);
									totalesNombre++;
									totalesDireccion++;
									totalesCuenta++;
									cargaCirculo.append("<Persona>\r");
									cargaCirculo.append("<Nombre>\r");
									cargaCirculo.append("<ApellidoPaterno>"+FormatUtil.deleteChar(infoCliente.aPaterno.trim().toUpperCase(),'.')+"</ApellidoPaterno>\r");
									cargaCirculo.append("<ApellidoMaterno>"+FormatUtil.deleteChar(infoCliente.aMaterno.trim().toUpperCase(),'.')+"</ApellidoMaterno>\r");
									cargaCirculo.append("<Nombres>"+FormatUtil.deleteChar(infoCliente.nombre.trim().toUpperCase(),'.')+"</Nombres>\r");
									cargaCirculo.append("<FechaNacimiento>"+Convertidor.dateToStringCirculo(infoCliente.fechaNacimiento)+"</FechaNacimiento>\r");
									cargaCirculo.append("<RFC>"+infoCliente.rfc+"</RFC>\r");
									cargaCirculo.append("</Nombre>\r");
									infoDireccion = direccionDAO.getDirecciones(infoCliente.idCliente);
									dir = direccionDAO.getDireccion(infoDireccion[0].idCliente,infoDireccion[0].idSolicitud,infoDireccion[0].tabla, infoDireccion[0].indiceTabla);
									String direccion = dir.calle.trim() + " " + dir.numeroExterior.trim() + " " + dir.numeroInterior.trim();
									direccion = direccion.trim().toUpperCase();
									cargaCirculo.append("<Domicilios>\r");
									cargaCirculo.append("<Domicilio>\r");
									cargaCirculo.append("<Direccion>"+FormatUtil.deleteChar(direccion,'.')+"</Direccion>\r");
									cargaCirculo.append("<ColoniaPoblacion>"+FormatUtil.deleteChar(dir.colonia.trim().toUpperCase(),'.')+"</ColoniaPoblacion>\r");
									cargaCirculo.append("<DelegacionMunicipio>"+FormatUtil.deleteChar(dir.municipio.trim().toUpperCase(),'.')+"</DelegacionMunicipio>\r");
									String numestado = String.valueOf(dir.numestado);
									cargaCirculo.append("<Estado>"+catalogo.get(numestado)+"</Estado>\r");
									cargaCirculo.append("<CP>"+dir.cp.trim()+"</CP>\r");
									cargaCirculo.append("</Domicilio>\r");
									cargaCirculo.append("</Domicilios>\r");
									cargaCirculo.append("<Empleos>\r");
									cargaCirculo.append("<Empleo>\r");
									cargaCirculo.append("</Empleo>\r");
									cargaCirculo.append("</Empleos>\r");
									cargaCirculo.append("<Cuenta>\r");
									cargaCirculo.append("<ClaveActualOtorgante>0003200045</ClaveActualOtorgante>\r");
									cargaCirculo.append("<NombreOtorgante>FINAFIRM</NombreOtorgante>\r");
									if ( clientes[j].fechaDisposicion.after(fechaInicioReferencias) )
										cargaCirculo.append("<CuentaActual>"+referencia+"</CuentaActual>\r");
									else{
										cargaCirculo.append("<CuentaActual>"+String.valueOf(infoCliente.idCliente)+"</CuentaActual>\r");
									}
									cargaCirculo.append("<TipoResponsabilidad>I</TipoResponsabilidad>\r");
									cargaCirculo.append("<TipoCuenta>F</TipoCuenta>\r");
									cargaCirculo.append("<TipoContrato>PP</TipoContrato>\r");
									cargaCirculo.append("<ClaveUnidadMonetaria>MX</ClaveUnidadMonetaria>\r");
									cargaCirculo.append("<NumeroPagos>"+String.valueOf(plazo)+"</NumeroPagos>\r");
									cargaCirculo.append("<FrecuenciaPagos>"+frecuencia+"</FrecuenciaPagos>\r");
									if( saldoActual < mensualidad )
										mensualidad=saldoActual;
									cargaCirculo.append("<MontoPagar>"+mensualidad.intValue()+"</MontoPagar>\r");
									cargaCirculo.append("<FechaAperturaCuenta>"+fechaDesembolso+"</FechaAperturaCuenta>\r");
									cargaCirculo.append("<FechaUltimoPago>"+fechaUltPago+"</FechaUltimoPago>\r");
									cargaCirculo.append("<FechaUltimaCompra>"+fechaDesembolso+"</FechaUltimaCompra>\r");
									if ( saldoActual.intValue() <= 0 && saldoVencido <= 200 ){
										cargaCirculo.append("<FechaCierreCuenta>"+periodoReporte+"</FechaCierreCuenta>\r");
										cuentasCerradas++;
									}
									cargaCirculo.append("<FechaCorte>"+periodoReporte+"</FechaCorte>\r");
									cargaCirculo.append("<CreditoMaximo>"+montoCredito.intValue()+"</CreditoMaximo>\r");
									saldoTotales += saldoActual;
									cargaCirculo.append("<SaldoActual>"+saldoActual.intValue()+"</SaldoActual>\r");
									cargaCirculo.append("<LimiteCredito>"+montoCredito.intValue()+"</LimiteCredito>\r");
									String sdoVencido = "0";
									if ( !MOP.equals(" V") ){
										if ( saldoVencido > saldoActual )
											saldoVencido = saldoActual;
										vencidosTotales += saldoVencido;
										sdoVencido = String.valueOf(Math.round(saldoVencido));
										contadorMorosos++;
									}
					
									cargaCirculo.append("<SaldoVencido>"+sdoVencido+"</SaldoVencido>\r");
									cargaCirculo.append("<NumeroPagosVencidos>"+pagVencidos+"</NumeroPagosVencidos>\r");								
									cargaCirculo.append("<PagoActual>"+MOP+"</PagoActual>\r");
									cargaCirculo.append("</Cuenta>\r");
									cargaCirculo.append("</Persona>\r");
					
									}else{
										noMatch.put(cont,RFC);
										cont = cont + 1;
								}
							}
						}
					}else{
						//System.out.println("Paso 6");
						numClientes++;
						infoCliente = clienteDAO.getCliente(clientes[j].numCliente);
						if ( infoCliente != null && infoCliente.fechaNacimiento != null ){
							registrosProcesados++;
							//fechaDesembolso = FormatUtil.convierteFecha(fechaDesembolso,0);
							fechaDesembolso = Convertidor.formatDateCirculo(fechaDesembolso);
							//fechaUltPago = FormatUtil.convierteFecha(fechaUltPago,0);
							fechaUltPago = Convertidor.formatDateCirculo(fechaUltPago);
							totalesNombre++;
							totalesDireccion++;
							totalesCuenta++;
							cargaCirculo.append("<Persona>\r");
							cargaCirculo.append("<Nombre>\r");
							cargaCirculo.append("<ApellidoPaterno>"+FormatUtil.deleteChar(infoCliente.aPaterno.trim().toUpperCase(),'.')+"</ApellidoPaterno>\r");
							cargaCirculo.append("<ApellidoMaterno>"+FormatUtil.deleteChar(infoCliente.aMaterno.trim().toUpperCase(),'.')+"</ApellidoMaterno>\r");
							cargaCirculo.append("<Nombres>"+FormatUtil.deleteChar(infoCliente.nombre.trim().toUpperCase(),'.')+"</Nombres>\r");
							cargaCirculo.append("<FechaNacimiento>"+Convertidor.dateToStringCirculo(infoCliente.fechaNacimiento)+"</FechaNacimiento>\r");
							cargaCirculo.append("<RFC>"+infoCliente.rfc+"</RFC>\r");
							cargaCirculo.append("</Nombre>\r");
							infoDireccion = direccionDAO.getDirecciones(infoCliente.idCliente);
							dir = direccionDAO.getDireccion(infoDireccion[0].idCliente,infoDireccion[0].idSolicitud,infoDireccion[0].tabla, infoDireccion[0].indiceTabla);
							String direccion = dir.calle.trim() + " " + dir.numeroExterior.trim() + " " + dir.numeroInterior.trim();
							direccion = direccion.trim().toUpperCase();
							cargaCirculo.append("<Domicilios>\r");
							cargaCirculo.append("<Domicilio>\r");
							cargaCirculo.append("<Direccion>"+FormatUtil.deleteChar(direccion,'.')+"</Direccion>\r");
							cargaCirculo.append("<ColoniaPoblacion>"+FormatUtil.deleteChar(dir.colonia.trim().toUpperCase(),'.')+"</ColoniaPoblacion>\r");
							cargaCirculo.append("<DelegacionMunicipio>"+FormatUtil.deleteChar(dir.municipio.trim().toUpperCase(),'.')+"</DelegacionMunicipio>\r");
							String numestado = String.valueOf(dir.numestado);
							cargaCirculo.append("<Estado>"+catalogo.get(numestado)+"</Estado>\r");
							cargaCirculo.append("<CP>"+dir.cp.trim()+"</CP>\r");
							cargaCirculo.append("</Domicilio>\r");
							cargaCirculo.append("</Domicilios>\r");
							cargaCirculo.append("<Empleos>\r");
							cargaCirculo.append("<Empleo>\r");
							cargaCirculo.append("</Empleo>\r");
							cargaCirculo.append("</Empleos>\r");
							cargaCirculo.append("<Cuenta>\r");
							cargaCirculo.append("<ClaveActualOtorgante>0003200045</ClaveActualOtorgante>\r");
							cargaCirculo.append("<NombreOtorgante>FINAFIRM</NombreOtorgante>\r");
							if ( clientes[j].fechaDisposicion.after(fechaInicioReferencias) )
								cargaCirculo.append("<CuentaActual>"+clientes[j].noCuenta+"</CuentaActual>\r");
							else{
								cargaCirculo.append("<CuentaActual>"+String.valueOf(infoCliente.idCliente)+"</CuentaActual>\r");
							}
							cargaCirculo.append("<TipoResponsabilidad>I</TipoResponsabilidad>\r");
							cargaCirculo.append("<TipoCuenta>F</TipoCuenta>\r");
							cargaCirculo.append("<TipoContrato>PP</TipoContrato>\r");
							cargaCirculo.append("<ClaveUnidadMonetaria>MX</ClaveUnidadMonetaria>\r");
							cargaCirculo.append("<NumeroPagos>"+String.valueOf(plazo)+"</NumeroPagos>\r");
							cargaCirculo.append("<FrecuenciaPagos>"+frecuencia+"</FrecuenciaPagos>\r");
							if( saldoActual < mensualidad )
								mensualidad=saldoActual;
							cargaCirculo.append("<MontoPagar>"+mensualidad.intValue()+"</MontoPagar>\r");
							cargaCirculo.append("<FechaAperturaCuenta>"+fechaDesembolso+"</FechaAperturaCuenta>\r");
							cargaCirculo.append("<FechaUltimoPago>"+fechaUltPago+"</FechaUltimoPago>\r");
							cargaCirculo.append("<FechaUltimaCompra>"+fechaDesembolso+"</FechaUltimaCompra>\r");
							if ( saldoActual.intValue() <= 0 && saldoVencido < 200 ){
								cargaCirculo.append("<FechaCierreCuenta>"+periodoReporte+"</FechaCierreCuenta>\r");
								cuentasCerradas++;
							}
							cargaCirculo.append("<FechaCorte>"+periodoReporte+"</FechaCorte>\r");
							cargaCirculo.append("<CreditoMaximo>"+montoCredito.intValue()+"</CreditoMaximo>\r");
							saldoTotales += saldoActual;
							cargaCirculo.append("<SaldoActual>"+saldoActual.intValue()+"</SaldoActual>\r");
							cargaCirculo.append("<LimiteCredito>"+montoCredito.intValue()+"</LimiteCredito>\r");
							String sdoVencido = "0";
							if ( !MOP.equals(" V") ){
								if ( saldoVencido > saldoActual )
									saldoVencido = saldoActual;
								vencidosTotales += saldoVencido;
								sdoVencido = String.valueOf(Math.round(saldoVencido));
								contadorMorosos++;
							}
			
							cargaCirculo.append("<SaldoVencido>"+sdoVencido+"</SaldoVencido>\r");
							cargaCirculo.append("<NumeroPagosVencidos>"+pagVencidos+"</NumeroPagosVencidos>\r");								
							cargaCirculo.append("<PagoActual>"+MOP+"</PagoActual>\r");
							cargaCirculo.append("</Cuenta>\r");
							cargaCirculo.append("</Persona>\r");
			
							}else{
								noMatch.put(cont,RFC);
								cont = cont + 1;
						}
					}
				}
			}
		}
		cargaCirculo.append("</Personas>\r");
		cargaCirculo.append("<CifrasControl>\r");
		cargaCirculo.append("<TotalSaldosActuales>"+saldoTotales.intValue()+"</TotalSaldosActuales>\r");
		cargaCirculo.append("<TotalSaldosVencidos>"+vencidosTotales.intValue()+"</TotalSaldosVencidos>\r");
		cargaCirculo.append("<TotalElementosNombreReportados>"+totalesNombre+"</TotalElementosNombreReportados>\r");
		cargaCirculo.append("<TotalElementosDireccionReportados>"+totalesDireccion+"</TotalElementosDireccionReportados>\r");
		cargaCirculo.append("<TotalElementosEmpleoReportados>"+totalesEmpleo+"</TotalElementosEmpleoReportados>\r");
		cargaCirculo.append("<TotalElementosCuentaReportados>"+totalesCuenta+"</TotalElementosCuentaReportados>\r");
		cargaCirculo.append("<NombreOtorgante>FINAFIRM</NombreOtorgante>\r");
		cargaCirculo.append("<DomicilioDevolucion>Rio Tiber no.100 piso 8 Col.Cuauhtemoc c.p.06500</DomicilioDevolucion>\r");
		cargaCirculo.append("</CifrasControl>\r");
		cargaCirculo.append("</Carga>\r");

		response.setContentType("application/xml");
		response.setHeader("Content-Disposition","attachment; filename=\"FINAFIRM-"+periodoReporte+".xml\"");
		response.setHeader("cache-control", "no-cache");
		String salidaArchivo = Convertidor.characterSet(cargaCirculo.toString());
		//if ( incSaldo == 0 && incMensualidad == 0 && incVencido == 0 )
		//	out.println(salidaArchivo);
		//else{
		//	out.println("SaldoActual\n" + incidenciaSaldoActual + "Mensualidad\n" + incidenciaMensualidad + "SaldoVencido\n" + incidenciaSaldoVencido);
		//}
		out.println(salidaArchivo);
		out.flush();
			
		System.out.println("No. Clientes reportados: " + registrosProcesados);
		System.out.println("No. Grupos Procesados: " + numGrupos);
		
		System.out.println("No. Registros morosos reportados: " + contadorMorosos);
		System.out.println("No. Cuentas cerradas: " + cuentasCerradas);
		}
		catch(Exception e){
			Logger.debug("Excepcion en getCliente : "+e.getMessage());
			e.printStackTrace();
		}
		array.add(new Notification( ClientesConstants.INFO_TYPE, "El archivo fue generado correctamente." ));%>
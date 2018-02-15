<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%><%@ page import="java.util.*"%><%@ page import="com.sicap.clientes.vo.*"%><%@ page import="com.sicap.clientes.dao.*"%><%@ page import="com.sicap.clientes.util.*"%><%@ page import="com.sicap.clientes.helpers.*"%><%@ page import="java.text.NumberFormat"%><jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" /><%
	ArrayList array = new ArrayList();
	mySmartUpload.initialize(pageContext);
	mySmartUpload.upload();
	NumberFormat NF = NumberFormat.getInstance();
	NF.setMaximumFractionDigits(2); 
	StringBuffer INTF = new StringBuffer();
	String periodoReporte = request.getParameter("periodo");
	
	Hashtable noMatch = new  Hashtable();
	int cont = 0;
	
	String incidenciaSaldoActual  = "";
	String incidenciaMensualidad  = "";
	String incidenciaSaldoVencido = "";
	int incSaldo = 0;
	int incMensualidad = 0;
	int incVencido = 0;

	Double montoCredito = null;
	Double mensualidad = null;
	Double saldoVencido = null;
//	Long saldoTotalVencido = new Long(0);
	Double saldoTotalVencido = null;
	Double saldoActual = null;
//	Long saldoTotalActual = new Long(0);
	Double saldoTotalActual = null;
	String fechaDesembolso = null;
	String fechaUltPago = "";
	String frecuencia = null;
	String RFC = "";
	Integer plazo = 0;
	Integer diasMora = 0;
	Integer pagVencidos = 0;
	int x = 0;
	int contadorMorosos = 0;
	int registrosProcesados = 0;
	int registrosTotales = 0;
	int cuentasCerradas = 0;
	int numGrupos = 0;
	int numClientes = 0;

	Calendar cal = Calendar.getInstance();
	Date fechaInicioReferencias = Convertidor.stringToDate(ClientesConstants.FECHA_INICIO_CUENTAS_BURO);
	int month = cal.get(Calendar.MONTH);
	month++;
	TreeMap catalogo = MapeoHelper.getCatalogoMapeo(2);
	SaldoT24DAO cartera = new SaldoT24DAO();
	SaldoT24VO[] clientes = null;
	BitacoraBuroCirculoTotalesVO bitTotalesVO = new BitacoraBuroCirculoTotalesVO();
	BitacoraBuroCirculoTotalesDAO bitTotalesDAO = new BitacoraBuroCirculoTotalesDAO();

	INTF.append("INTF10FF28190001FINAFIRM          ");
	INTF.append(periodoReporte);
	INTF.append("0000000000" + FormatUtil.completaCadena(null, ' ' ,98,"L"));

	try{
		for ( int paso=1; paso<5; paso++){
			//Obtiene la cartera total de SaldosT24
			clientes = cartera.getVSaldosT24( paso );
			
			for( int j=0; j<clientes.length; j++ ){
				//Si el cliente no está cancelado
				if ( clientes[j].situacionActualCredito != 4 ){
					//registrosProcesados++;
					Date pagoMax = new Date();
					RFC = clientes[j].rfc;
					montoCredito = clientes[j].montoLineaCredito;
					mensualidad = clientes[j].montoAmortizacion + clientes[j].montoInteresesPorCobrar;
					saldoVencido = clientes[j].capitalVencido;
					saldoActual = clientes[j].saldoInsoluto;
					fechaDesembolso = Convertidor.dateToString(clientes[j].fechaDisposicion);
					pagoMax = new PagoDAO().getUltimoPago(clientes[j].noCuenta);
					
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
					diasMora = clientes[j].diasVencidos;
					pagVencidos = clientes[j].numeroPagosVencidos;
					
					if ( mensualidad == 0 && saldoActual > 0 && clientes[j].situacionActualCredito == 2 )
						mensualidad = clientes[j].totalExigible;
					if ( saldoActual == 0 )
						diasMora = 0;
	
					if ( saldoVencido == 0 )
						diasMora = 0;
					if ( clientes[j].situacionActualCredito == 3 ){
						saldoActual = 0.00;
						diasMora = 0;
					}
	
					String MOP = "01";
					frecuencia = "M";
	
					//Calcula el MOP dependiendo del número de días de mora
					if ( diasMora>0 ){
						x = new Integer(diasMora);
						if (x >= 1   && x <= 29)  		MOP = "02";
						if (x >= 30  && x <= 59) 		MOP = "03";
						if (x >= 60  && x <= 89) 		MOP = "04";
						if (x >= 90  && x <= 119)		MOP = "05";
						if (x >= 120 && x <= 179)		MOP = "06";
						if (x >= 180)					MOP = "07";
					}
					
					ClienteDAO clienteDAO = new ClienteDAO();
					DireccionDAO direccionDAO = new DireccionDAO();
					BitacoraBuroCirculoDAO bitDAO = new BitacoraBuroCirculoDAO();
					ClienteVO  infoCliente = null;
	
					DireccionVO[] infoDireccion = null;
					DireccionVO dir = null;
	
					if( saldoActual == 0 && !MOP.equals("01") ){
						System.out.println("RFC: " + RFC + " SaldoActual: " + saldoActual);
						incidenciaSaldoActual+=RFC + "\n";
						incSaldo++;
					}
					
					if( mensualidad == 0 && saldoActual > 0 ){
						System.out.println("RFC: " + RFC + "Mensualidad: " + mensualidad + "SaldoActual: " + saldoActual);
						incidenciaMensualidad+=RFC + "\n";
						incMensualidad++;
					}
					
					if( saldoVencido == 0 && !MOP.equals("01") ){
						System.out.println("RFC: " + RFC + " saldoVencido: " + saldoVencido);
						incidenciaSaldoVencido+=RFC + "\n";
						incVencido++;
					}
					
					if ( clientes[j].numOperacion == ClientesConstants.GRUPAL ){
						BitacoraBuroCirculoVO bitBuroCirculo = new BitacoraBuroCirculoVO();
						bitBuroCirculo.estatus = ClientesConstants.ACTIVO;
						java.util.Date fecha = new java.util.Date();
						fecha = Convertidor.stringToDate(fechaDesembolso);
						fechaDesembolso = FormatUtil.deleteChar(Convertidor.dateToStringBuro(fecha),'/');
						fecha = Convertidor.stringToDate(fechaUltPago);
						fechaUltPago = FormatUtil.deleteChar(Convertidor.dateToStringBuro(fecha),'/');
						numGrupos++;
						IntegranteCicloVO[] integrantes = new IntegranteCicloDAO().getIntegrantes( clientes[j].numCliente, clientes[j].ciclo );
						if ( integrantes!= null ){
							for ( int y=0; y<integrantes.length; y++ ){
								infoCliente = new ClienteDAO().getCliente( integrantes[y].idCliente );
								if ( infoCliente != null && infoCliente.fechaNacimiento != null ){
									registrosProcesados++;
									String referencia = ClientesUtil.makeReferencia(infoCliente, integrantes[y].idSolicitud );
									String mtoMensual = String.valueOf(mensualidad.intValue());
									INTF.append("PN"+FormatUtil.completaCadena(String.valueOf(infoCliente.aPaterno.trim().length()), '0' ,2,"L")+infoCliente.aPaterno.trim());
									INTF.append("00"+FormatUtil.completaCadena(String.valueOf(infoCliente.aMaterno.trim().length()), '0' ,2,"L")+infoCliente.aMaterno.trim());
									INTF.append("02"+FormatUtil.completaCadena(String.valueOf(infoCliente.nombre.trim().length()), '0' ,2,"L")+infoCliente.nombre.trim());
									INTF.append("0408" + FormatUtil.deleteChar(Convertidor.dateToStringBuro(infoCliente.fechaNacimiento),'/'));
									INTF.append("05" + FormatUtil.completaCadena(String.valueOf(infoCliente.rfc.length()), '0' ,2,"L")+infoCliente.rfc.trim());
									infoDireccion = direccionDAO.getDirecciones(infoCliente.idCliente);
									dir = direccionDAO.getDireccion(infoDireccion[0].idCliente,infoDireccion[0].idSolicitud,infoDireccion[0].tabla, infoDireccion[0].indiceTabla);
									String direccion = dir.calle.trim() + " " + dir.numeroExterior.trim() + " " + dir.numeroInterior.trim();
									direccion = direccion.trim();
									INTF.append("PA" + FormatUtil.completaCadena(String.valueOf(direccion.trim().length()), '0' ,2,"L") + direccion.trim());
									INTF.append("01" + FormatUtil.completaCadena(String.valueOf(dir.colonia.trim().length()),'0',2,"L") + dir.colonia.trim());
									INTF.append("02" + FormatUtil.completaCadena(String.valueOf(dir.municipio.trim().length()),'0',2,"L") + dir.municipio.trim());
									String numestado = String.valueOf(dir.numestado);
									String estado = (String)catalogo.get(numestado);
									INTF.append("04" + FormatUtil.completaCadena(String.valueOf(estado.length()),'0',2,"L") + estado.trim());
									INTF.append("0505" + FormatUtil.completaCadena(dir.cp,'0',5,"L"));
									INTF.append("TL02TL0110FF281900010208FINAFIRM04");
									String cuenta = "";
									
									if ( clientes[j].fechaDisposicion.after(fechaInicioReferencias) )
										cuenta = referencia;
									else{
										cuenta = String.valueOf(infoCliente.idCliente);
										cuenta = FormatUtil.completaCadena(cuenta,'0',4,"L");
									}
									
									INTF.append(FormatUtil.completaCadena(String.valueOf(cuenta.length()),'0',2,"L")+cuenta);
									INTF.append("0601I0702CS0802MX10");
									String pagos = String.valueOf(plazo);
									INTF.append(FormatUtil.completaCadena(String.valueOf(pagos.length()),'0',2,"L")+pagos);
									INTF.append("1101"+frecuencia);
									String saldo = String.valueOf(saldoActual.intValue());
									saldoTotalActual += saldoActual;
									if( saldoActual<=0 )
										mtoMensual = "0";
									else if ( saldoActual<mensualidad )
										mtoMensual = saldo;
									INTF.append("12"+FormatUtil.completaCadena(String.valueOf(mtoMensual.length()),'0',2,"L")+mtoMensual);
									INTF.append("1308"+fechaDesembolso);
									INTF.append("1408"+fechaUltPago);
									INTF.append("1508"+fechaUltPago);
									if ( saldoActual == 0 && saldoVencido < 200 ){
										INTF.append("1608"+periodoReporte);
										cuentasCerradas++;
									}
									String monto = String.valueOf(montoCredito.intValue());
									INTF.append("21"+FormatUtil.completaCadena(String.valueOf(monto.length()),'0',2,"L")+monto);
									INTF.append("22"+FormatUtil.completaCadena(String.valueOf(saldo.length()),'0',2,"L")+saldo);
									if ( !MOP.equals("01") ){
										if ( saldoVencido > saldoActual )
											saldoVencido = saldoActual;
										saldoTotalVencido += saldoVencido;
										String sdoVencido = String.valueOf(saldoVencido.intValue());
										INTF.append("24"+FormatUtil.completaCadena(String.valueOf(sdoVencido.length()),'0',2,"L")+sdoVencido);
										INTF.append("2502"+FormatUtil.completaCadena(pagVencidos.toString(),'0',2,"L"));
										contadorMorosos++;
									}
									INTF.append("2602"+MOP+"9903END");
				
									//if(referencia.equalsIgnoreCase("0000000000000"));
									//	referencia = ClientesUtil.makeReferencia(infoCliente, 1);
								}else{
									noMatch.put(cont,RFC);
									cont = cont + 1;
								}
							}
						}
							bitBuroCirculo.numCuenta = clientes[j].numCliente;
							if ( clientes[j].noCuenta.length() > 13 )
								bitBuroCirculo.referencia = clientes[j].noCuenta.substring( 1,13 );
							else
								bitBuroCirculo.referencia = clientes[j].noCuenta;
							bitBuroCirculo.mop = MOP;
							bitBuroCirculo.numDiasMora = diasMora;
							bitBuroCirculo.saldoActual = saldoActual;
							bitBuroCirculo.saldoVencido = saldoVencido;
							if ( clientes[j].fechaDisposicion.after(fechaInicioReferencias) )
								bitBuroCirculo.cuentaEnviada = 1;
							BitacoraBuroCirculoDAO insertBitDAO = new BitacoraBuroCirculoDAO();
							insertBitDAO.add(bitBuroCirculo);

					}else{
						numClientes++;
						infoCliente = clienteDAO.getCliente(clientes[j].numCliente);
						if ( infoCliente != null && infoCliente.fechaNacimiento != null ){
							registrosProcesados++;
							BitacoraBuroCirculoVO bitBuroCirculo = new BitacoraBuroCirculoVO();
							bitBuroCirculo.estatus = ClientesConstants.ACTIVO;
							java.util.Date fecha = new java.util.Date();
							fecha = Convertidor.stringToDate(fechaDesembolso);
							fechaDesembolso = FormatUtil.deleteChar(Convertidor.dateToStringBuro(fecha),'/');
							fecha = Convertidor.stringToDate(fechaUltPago);
							fechaUltPago = FormatUtil.deleteChar(Convertidor.dateToStringBuro(fecha),'/');
							String mtoMensual = String.valueOf(mensualidad.intValue());
							
							INTF.append("PN"+FormatUtil.completaCadena(String.valueOf(infoCliente.aPaterno.trim().length()), '0' ,2,"L")+infoCliente.aPaterno.trim());
							INTF.append("00"+FormatUtil.completaCadena(String.valueOf(infoCliente.aMaterno.trim().length()), '0' ,2,"L")+infoCliente.aMaterno.trim());
							INTF.append("02"+FormatUtil.completaCadena(String.valueOf(infoCliente.nombre.trim().length()), '0' ,2,"L")+infoCliente.nombre.trim());
							INTF.append("0408" + FormatUtil.deleteChar(Convertidor.dateToStringBuro(infoCliente.fechaNacimiento),'/'));
							INTF.append("05" + FormatUtil.completaCadena(String.valueOf(infoCliente.rfc.length()), '0' ,2,"L")+infoCliente.rfc.trim());
							infoDireccion = direccionDAO.getDirecciones(infoCliente.idCliente);
							dir = direccionDAO.getDireccion(infoDireccion[0].idCliente,infoDireccion[0].idSolicitud,infoDireccion[0].tabla, infoDireccion[0].indiceTabla);
							String direccion = dir.calle.trim() + " " + dir.numeroExterior.trim() + " " + dir.numeroInterior.trim();
							direccion = direccion.trim();
							INTF.append("PA" + FormatUtil.completaCadena(String.valueOf(direccion.trim().length()), '0' ,2,"L") + direccion.trim());
							INTF.append("01" + FormatUtil.completaCadena(String.valueOf(dir.colonia.trim().length()),'0',2,"L") + dir.colonia.trim());
							INTF.append("02" + FormatUtil.completaCadena(String.valueOf(dir.municipio.trim().length()),'0',2,"L") + dir.municipio.trim());
							String numestado = String.valueOf(dir.numestado);
							String estado = (String)catalogo.get(numestado);
							INTF.append("04" + FormatUtil.completaCadena(String.valueOf(estado.length()),'0',2,"L") + estado.trim());
							INTF.append("0505" + FormatUtil.completaCadena(dir.cp,'0',5,"L"));
							INTF.append("TL02TL0110FF281900010208FINAFIRM04");
							String cuenta = "";
							
							if ( clientes[j].fechaDisposicion.after(fechaInicioReferencias) )
								cuenta = clientes[j].noCuenta;
							else{
								cuenta = String.valueOf(infoCliente.idCliente);
								cuenta = FormatUtil.completaCadena(cuenta,'0',4,"L");
							}
							
							INTF.append(FormatUtil.completaCadena(String.valueOf(cuenta.length()),'0',2,"L")+cuenta);
							INTF.append("0601I0702CS0802MX10");
							String pagos = String.valueOf(plazo);
							INTF.append(FormatUtil.completaCadena(String.valueOf(pagos.length()),'0',2,"L")+pagos);
							INTF.append("1101"+frecuencia);
							String saldo = String.valueOf(saldoActual.intValue());
							saldoTotalActual += saldoActual;
							if( saldoActual<=0 )
								mtoMensual = "0";
							else if ( saldoActual<mensualidad )
								mtoMensual = saldo;
							INTF.append("12"+FormatUtil.completaCadena(String.valueOf(mtoMensual.length()),'0',2,"L")+mtoMensual);
							INTF.append("1308"+fechaDesembolso);
							INTF.append("1408"+fechaUltPago);
							INTF.append("1508"+fechaUltPago);
							if ( saldoActual == 0 && saldoVencido < 10 ){
								INTF.append("1608"+periodoReporte);
								cuentasCerradas++;
								bitBuroCirculo.estatus = ClientesConstants.LIQUIDADO;						
							}
							String monto = String.valueOf(montoCredito.intValue());
							INTF.append("21"+FormatUtil.completaCadena(String.valueOf(monto.length()),'0',2,"L")+monto);
							INTF.append("22"+FormatUtil.completaCadena(String.valueOf(saldo.length()),'0',2,"L")+saldo);
							if ( !MOP.equals("01") ){
								if ( saldoVencido > saldoActual )
									saldoVencido = saldoActual;
								saldoTotalVencido += saldoVencido;
								String sdoVencido = String.valueOf(saldoVencido.intValue());
								INTF.append("24"+FormatUtil.completaCadena(String.valueOf(sdoVencido.length()),'0',2,"L")+sdoVencido);
								INTF.append("2502"+FormatUtil.completaCadena(pagVencidos.toString(),'0',2,"L"));
								contadorMorosos++;
							}
							INTF.append("2602"+MOP+"9903END");
		
							bitBuroCirculo.numCuenta = infoCliente.idCliente;
							//if(referencia.equalsIgnoreCase("0000000000000"));
							//	referencia = ClientesUtil.makeReferencia(infoCliente, 1);
							if ( clientes[j].noCuenta.length() > 13 )
								bitBuroCirculo.referencia = clientes[j].noCuenta.substring( 1,13 );
							else
								bitBuroCirculo.referencia = clientes[j].noCuenta;
							bitBuroCirculo.mop = MOP;
							bitBuroCirculo.numDiasMora = diasMora;
							bitBuroCirculo.saldoActual = saldoActual;
							bitBuroCirculo.saldoVencido = saldoVencido;
							if ( clientes[j].fechaDisposicion.after(fechaInicioReferencias) )
								bitBuroCirculo.cuentaEnviada = 1;
							BitacoraBuroCirculoDAO insertBitDAO = new BitacoraBuroCirculoDAO();
							insertBitDAO.add(bitBuroCirculo);
						}else{
								noMatch.put(cont,RFC);
								cont = cont + 1;
						}

					}
				}
			}
		}
		String strSaldoTotalActual = String.valueOf(saldoTotalActual);
		INTF.append("TRLR");
		INTF.append(FormatUtil.completaCadena(String.valueOf(strSaldoTotalActual),'0',14,"L"));
		String strSaldoTotalVencido = String.valueOf(saldoTotalVencido);
		INTF.append(FormatUtil.completaCadena(String.valueOf(strSaldoTotalVencido),'0',14,"L"));
		INTF.append("001");
		INTF.append(FormatUtil.completaCadena(String.valueOf(registrosTotales),'0',9,"L"));
		INTF.append(FormatUtil.completaCadena(String.valueOf(registrosTotales),'0',9,"L"));
		INTF.append("000000000");
		INTF.append(FormatUtil.completaCadena(String.valueOf(registrosTotales),'0',9,"L"));
		INTF.append("000000000");
		INTF.append(FormatUtil.completaCadena("FINAFIRM",' ',16,"R"));
		INTF.append(FormatUtil.completaCadena("RIO TIBER NO.100 PISO 8 COL.CUAUHTEMOC C.P.06500",' ',160,"R"));
		
		String salidaArchivo = Convertidor.characterSet(INTF.toString().toUpperCase());
		response.setContentType("application/txt");
		response.setHeader("Content-Disposition","attachment; filename=\"actualizaBuro.txt\"");
		response.setHeader("cache-control", "no-cache");
		//if ( incSaldo == 0 && incMensualidad == 0 && incVencido == 0 ){
		//	System.out.println("No. Registros procesados: " + registrosProcesados);
		//	System.out.println("No. Clientes no encontrados: " + noMatch.size());
		//	System.out.println("No. Registros morosos reportados: " + contadorMorosos);
		//	System.out.println("No. Cuentas cerradas: " + cuentasCerradas);
		//	System.out.println("No. Registros totales enviados: " + registrosTotales);
			
		//	bitTotalesVO.totalCuentas = registrosProcesados;
		//	bitTotalesVO.totalCuentasMora = contadorMorosos;
		//	bitTotalesVO.totalCuentasCerradas = cuentasCerradas;
		//	bitTotalesVO.totalSaldosActuales = saldoTotalActual;
		//	bitTotalesVO.totalSaldosVencidos = saldoTotalVencido;
		//	bitTotalesDAO.add(bitTotalesVO);
			
		//	out.println(salidaArchivo);
		//}else{
		//	out.println(			"----------SaldoActual----------\n" + 
		//	incidenciaSaldoActual + "----------Mensualidad----------\n" + 
		//	incidenciaMensualidad + "----------SaldoVencido---------\n" + 
		//	incidenciaSaldoVencido);
		//}
		
		System.out.println("No. Registros procesados: " + registrosProcesados);
		System.out.println("No. Clientes no encontrados: " + noMatch.size());
		System.out.println("No. Registros morosos reportados: " + contadorMorosos);
		System.out.println("No. Cuentas cerradas: " + cuentasCerradas);
		System.out.println("No. Registros totales enviados: " + registrosTotales);
		
		bitTotalesVO.totalCuentas = registrosProcesados;
		bitTotalesVO.totalCuentasMora = contadorMorosos;
		bitTotalesVO.totalCuentasCerradas = cuentasCerradas;
		bitTotalesVO.totalSaldosActuales = saldoTotalActual;
		bitTotalesVO.totalSaldosVencidos = saldoTotalVencido;
		bitTotalesDAO.add(bitTotalesVO);
		
		out.println(salidaArchivo);
		
		out.flush();
		
		System.out.println("No. Clientes Reportados: " + registrosProcesados);
		System.out.println("No. Grupos Procesados: " + numGrupos);
		
		System.out.println("No. Registros morosos reportados: " + contadorMorosos);
		System.out.println("No. Cuentas cerradas: " + cuentasCerradas);
	}
	catch(Exception e){
		System.out.println("Excepcion en Main CargaBuro : "+ RFC +e.getMessage());
		e.printStackTrace();
	}
	array.add(new Notification( ClientesConstants.INFO_TYPE, "El archivo fue generado correctamente." ));%>
package com.sicap.clientes.helpers.cartera;

import com.sicap.clientes.dao.CatalogoDAO;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.PagoPagareDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.SeguroHelper;
import com.sicap.clientes.helpers.cartera.EventoHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ComisionVO;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TasaInteresVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.PagoPagareVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import java.sql.Connection;

public class CreditoHelperCartera {

    public static boolean registraCreditoCartera(SolicitudVO solicitud, ClienteVO cliente, HttpServletRequest request, Vector notificaciones) throws Exception {

        int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, solicitud.idSolicitud);
        boolean result = false;
        HttpSession session = request.getSession();
        CreditoCartVO objlCredit = new CreditoCartVO();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
//		loadSources();


        try {
            objlCredit = getCreditInfo(cliente, solicitud, request.getRemoteUser());
            creditoDAO.addCredito(objlCredit);
            solicitud.idCreditoIBS = (objlCredit.getNumCredito());
            solicitud.idCuentaIBS = 1;
            if (solicitud.idCreditoIBS != 0) {
                new SolicitudDAO().updateSolicitudCredito(solicitud);
                cliente.solicitudes[indiceSolicitud] = solicitud;
                session.setAttribute("CLIENTE", cliente);
                result = true;
            }

        } catch (Exception e) {
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se logró registrar el CRÉDITO en el sistema de cartera IBS -- " + e.getMessage()));
            e.printStackTrace();
        }

        return result;
    }

    public boolean registraCreditoGrupo(GrupoVO grupo, Double montoDepositado, Double montoCuenta, CicloGrupalVO ciclo, HttpServletRequest request, Vector notificaciones) throws Exception {

        boolean bandera = true;
        HttpSession session = request.getSession();
        CreditoCartVO objlCredit = new CreditoCartVO();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();

        try {
            objlCredit = getCreditGroupInfo(grupo, montoDepositado, montoCuenta, ciclo, request.getRemoteUser());
            creditoDAO.addCredito(objlCredit);
            if (ciclo.idCreditoIBS != 0) {
                new CicloGrupalDAO().updateCicloCredito(ciclo);
                grupo.ciclos[ciclo.idCiclo - 1] = ciclo;
                session.setAttribute("GRUPO", grupo);
            }

        } catch (Exception e) {
            bandera = false;
            //notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE ,"No se logr� registrar el CR�DITO en el sistema de cartera IBS -- " + e.getMessage());
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se logró registrar el CRÉDITO en el sistema de cartera IBS -- " + e.getMessage()));
            e.printStackTrace();
        }

        return bandera;
    }

    public static boolean actualizaSaldoCuenta(String referencia, double pago) throws Exception {
        CreditoCartVO objlCredit = new CreditoCartVO();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        double monto_nuevo = 0;
        int res = 0;
        boolean result = true;

        try {
            objlCredit = creditoDAO.getCreditoReferencia(referencia);
            monto_nuevo = objlCredit.getMontoCuenta() + pago;
            objlCredit.setMontoCuenta(monto_nuevo);
            res = creditoDAO.updatePagoCredito(objlCredit);
        } catch (Exception e) {
            Logger.debug("Excepcion en actualizaSaldoCuenta : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());

        }

        return result;
    }

    public static boolean actualizaSaldoCuenta(PagoVO pago) throws Exception {
        CreditoCartVO objlCredit = new CreditoCartVO();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        EventoHelper eventohelper = new EventoHelper();
        double monto_nuevo = 0;
        int res = 0;
        boolean result = true;

        try {
            objlCredit = creditoDAO.getCreditoReferencia(pago.referencia);
            monto_nuevo = objlCredit.getMontoCuenta() + pago.monto;
            objlCredit.setMontoCuenta(monto_nuevo);
            eventohelper.registraPago(objlCredit, pago);
            res = creditoDAO.updatePagoCredito(objlCredit);//ACTUALIZA EL MONTO DEL GRUPO
// Se inserta el registro de pago en eventos JBL JUN/10			
        } catch (Exception e) {
            Logger.debug("Excepcion en actualizaSaldoCuenta : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());

        }

        return result;
    }

    public static boolean actualizaSaldoCuentaCierre(PagoVO pago, CreditoCartVO credito) throws Exception {
        
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        //EventoHelper eventohelper = new EventoHelper();
        boolean result = true;

        try {
            credito.setMontoCuenta(credito.getMontoCuenta()+pago.getMonto());
            /*if(creditoDAO.updatePagoCreditoCierre(credito)==1){
                if(saldoDAO.updatePagoSaldoCierre(credito)==1)
                    result = true;
            }*/
            if(creditoDAO.updatePagoCreditoCierre(credito)==1)
                result = true;
            
        } catch (Exception e) {
            Logger.debug("Excepcion en actualizaSaldoCuentaCierre : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());

        }

        return result;
    }
    
    public static void actualizaSaldoCuenta(CreditoCartVO credito) throws Exception {
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        int res = 0;

        try {
            res = creditoDAO.updatePagoCredito(credito);

        } catch (Exception e) {
            Logger.debug("Excepcion en actualizaSaldoCuenta : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());

        }

    }

    private static CreditoCartVO getCreditInfo(ClienteVO cliente, SolicitudVO solicitud, String user) {
        Calendar cal = Calendar.getInstance();
        CreditoCartVO objlCredit = new CreditoCartVO();
        CreditoCartDAO creditDAO = new CreditoCartDAO();
        Double tasaIva = 0.000;
        TasaInteresVO tasaInteres = null;
        ComisionVO comision = null;
        boolean esReestructura = solicitud.reestructura == 1 ? true : false;

        try {
            SucursalVO sucursal = new SucursalDAO().getSucursal(cliente.idSucursal);
            if (Convertidor.esFronterizo(cliente.idSucursal)) {
                tasaIva = ClientesConstants.TASA_IVA_FRONTERIZO;
            } else {
                tasaIva = ClientesConstants.TASA_IVA_NO_FRONTERIZO;
            }

            TreeMap<Integer, ComisionVO> catComisiones = CatalogoHelper.getCatalogoComisiones(solicitud.tipoOperacion);
            TreeMap<Integer, TasaInteresVO> catTasas = CatalogoHelper.getCatalogoTasas(solicitud.tipoOperacion);
            double primaConComision = SeguroHelper.obtenPrimaConComision(solicitud, catComisiones);
            tasaInteres = catTasas.get(solicitud.decisionComite.tasa);
            comision = catComisiones.get(solicitud.decisionComite.comision);
            objlCredit.setNumCredito(creditDAO.getMaxCredito() + 1);
            objlCredit.setNumCliente((cliente.idCliente));
            objlCredit.setNumSolicitud(solicitud.idSolicitud);
            objlCredit.setReferencia(solicitud.referencia);
            objlCredit.setNumCuenta((solicitud.idCuentaIBS));
            objlCredit.setNumSucursal((cliente.idSucursal));
            objlCredit.setNumEmpresa(solicitud.numrepresentante);
            objlCredit.setFechaDesembolso(solicitud.fechaDesembolso);
            objlCredit.setFechaVencimiento(solicitud.amortizacion[solicitud.amortizacion.length - 1].fechaPago);
            objlCredit.setValorCuota((solicitud.amortizacion[1].montoPagar));
            objlCredit.setNumCuotas((solicitud.amortizacion.length - 1));
            objlCredit.setPeriodicidad((solicitud.decisionComite.frecuenciaPago));
            objlCredit.setMontoCredito((solicitud.decisionComite.montoAutorizado + primaConComision));
            objlCredit.setMontoDesembolsado((ClientesUtil.calculaMontoSinComision(solicitud.decisionComite.montoAutorizado, solicitud.decisionComite.comision, catComisiones)));
            //Modificar en cuanto se reactiven los seguros
//			objlCredit.setMontoSeguro( (solicitud.seguro.primaTotal) );
            objlCredit.setMontoSeguro((0));
            objlCredit.setMontoComision((solicitud.amortizacion[0].comisionInicial));
            objlCredit.setMontoIvaComision((solicitud.amortizacion[0].ivaComision));
            objlCredit.setTasaInteresSIVA((solicitud.tasaCalculada / (1 + tasaIva)));
            objlCredit.setTasaInteres((solicitud.tasaCalculada));
            objlCredit.setTasaMora((solicitud.tasaCalculada * 2 / (1 + tasaIva)));
            objlCredit.setTasaComision((comision.porcentaje / (1 + tasaIva)));
            objlCredit.setTasaIVA(tasaIva * 100);
            objlCredit.setNumProducto((solicitud.tipoOperacion));

            //********************Modificar para parametrizar con cat�logo de fondeadores
            objlCredit.setFondeador(1);
            //********************Modificar para parametrizar con cat�logo de bancos
            objlCredit.setBancoDesembolso(sucursal.idBanco);
            if (solicitud.reestructura == 1) {
                String referenciaAnterior = new ReferenciaGeneralDAO().getReferencia(cliente.idCliente, solicitud.solicitudReestructura, 'I');
                objlCredit.setBancoDesembolso(9);
            }
            objlCredit.setNumDocumento((solicitud.numCheque));
            objlCredit.setStatus(1);
            objlCredit.setNumEjecutivo(solicitud.idEjecutivo);
            objlCredit.setValorCuota(solicitud.cuota);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }

    private static CreditoCartVO getCreditGroupInfo(GrupoVO grupo, Double montoDepositado, Double montoCuenta, CicloGrupalVO ciclo, String user) {
        Calendar cal = Calendar.getInstance();
        CreditoCartVO objlCredit = new CreditoCartVO();
        CreditoCartDAO creditDAO = new CreditoCartDAO();
        Double tasaIva = 0.000;
        Double montoSeguro = 0.00;
        TasaInteresVO tasaInteres = null;
        ComisionVO comision = null;
        double montoDesembolso = 0.00;
        double porGarantia = 0.00;
        String strGarantia = "";

        try {
            TreeMap<Integer, ComisionVO> catComisiones = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            TreeMap<Integer, TasaInteresVO> catTasas = CatalogoHelper.getCatalogoTasas(ClientesConstants.GRUPAL);

            SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);

            if (Convertidor.esFronterizo(grupo.sucursal)) {
                tasaIva = ClientesConstants.TASA_IVA_FRONTERIZO;
            } else {
                tasaIva = ClientesConstants.TASA_IVA_NO_FRONTERIZO;
            }

            for (int i = 0; i < ciclo.integrantes.length; i++) {
                montoSeguro += ciclo.integrantes[i].primaSeguro;
                // Credito Monto sin seguro Agosto 2017
                montoDesembolso += ClientesUtil.calculaMontoSinComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisiones);
            }

            tasaInteres = catTasas.get(ciclo.tasa);
            comision = catComisiones.get(ciclo.comision);
            objlCredit.setNumCredito(creditDAO.getMaxCredito() + 1);
            objlCredit.setNumCliente(grupo.idGrupo);
            objlCredit.setNumSolicitud(ciclo.idCiclo);
            objlCredit.setReferencia(ciclo.referencia);
            objlCredit.setNumSucursal(grupo.sucursal);
            objlCredit.setFechaDesembolso((ciclo.tablaAmortizacion[0].fechaPago));
//			objlCredit.setFechaAmortizacion1((ciclo.tablaAmortizacion[1].fechaPago)) );
//			objlCredit.setFechaAmortizacion2( Convertidor.formatDateCirculo(Convertidor.dateToString(ciclo.tablaAmortizacion[2].fechaPago)) );
            objlCredit.setFechaVencimiento((ciclo.tablaAmortizacion[ciclo.tablaAmortizacion.length - 1].fechaPago));
            objlCredit.setValorCuota(ciclo.tablaAmortizacion[1].montoPagar);
            objlCredit.setNumCuotas(ciclo.tablaAmortizacion.length - 1);
            objlCredit.setPeriodicidad(ClientesConstants.PAGO_SEMANAL);
            objlCredit.setMontoCredito((ciclo.montoConComision));
//			Se agrega la variable para el llevado del campo de monto Congelado
			/*objlCredit.setMontoCuenta(montoCuenta);
            objlCredit.setMontoCuentaCongelada(montoDepositado);*/
            objlCredit.setMontoCuenta(montoDepositado);
            //objlCredit.setMontoCuentaCongelada(ciclo.monto * 0.10);   SE CAMBIA A GARANTIA DINAMICA
            strGarantia = new CatalogoDAO().getGarantia(ciclo.garantia);
            porGarantia = Double.parseDouble(strGarantia);
            porGarantia = porGarantia/100;
            objlCredit.setMontoCuentaCongelada(montoDesembolso * porGarantia);
            objlCredit.setMontoDesembolsado((montoDesembolso));
            objlCredit.setMontoSeguro((montoSeguro));
            objlCredit.setMontoComision(ciclo.tablaAmortizacion[0].comisionInicial);
            objlCredit.setMontoIvaComision(ciclo.tablaAmortizacion[0].ivaComision);
            objlCredit.setTasaInteres(ciclo.tasaCalculada);
            objlCredit.setTasaInteresSIVA((ciclo.tasaCalculada / (1 + tasaIva)));
            objlCredit.setTasaMora((ciclo.tasaCalculada * ClientesConstants.FACTOR_MORA_GRUPO));
            // Se le quita el costo del seguro al monto comision para el calculo de la tasa comision Agosto 2017
            double costoSeguro = Math.abs((ciclo.montoConComision - ciclo.monto));
            objlCredit.setTasaComision((100 - ((ciclo.monto * 100) / (ciclo.montoConComision - costoSeguro))));
            objlCredit.setTasaIVA(tasaIva * 100);
            objlCredit.setNumProducto(ClientesConstants.GRUPAL);
            //********************Modificar para parametrizar con cat�logo de fondeadores
            objlCredit.setFondeador(ciclo.fondeador);
            objlCredit.setStatus(1);
            //********************Modificar para parametrizar con cat�logo de bancos
//			if ( sucursal.idBanco == 2 )
//				objlCredit.setBancoDesembolso( FormatUtil.completaCadena("72", '0', 4, "L") );
//			else if ( sucursal.idBanco == 0 )
//				objlCredit.setBancoDesembolso( FormatUtil.completaCadena("21", '0', 4, "L") );
            if (grupo.idOperacion == ClientesConstants.REESTRUCTURA_GRUPAL) {
                String referenciaAnterior = new ReferenciaGeneralDAO().getReferencia(grupo.idGrupoOriginal, grupo.idCicloOriginal, 'G');
//				objlCredit.setReferenciaReestructurado(referenciaAnterior.replace("LD", "99"));
//				objlCredit.setBancoDesembolso( "5000" );
//				objlCredit.setProducto( FormatUtil.completaCadena( "0006", '0', 4, "L") );
            }
//			objlCredit.setBancoDesembolso( FormatUtil.completaCadena("62", '0', 4, "L") );
//			objlCredit.setOrdenPago( ciclo.referencia );
//			objlCredit.setNumeroDesembolso( String.valueOf(ciclo.idCiclo) );
//			objlCredit.setUser( user );
//			objlCredit.setFechaSistema( Convertidor.formatDateCirculo(Convertidor.dateToString(cal.getTime())) );
//			objlCredit.setHoraSistema( FormatUtil.deleteChar(HTMLHelper.getHoraActual(), ':') );
            objlCredit.setNumDocumento((ciclo.referencia));
            objlCredit.setStatus(1);
            objlCredit.setNumEjecutivo(ciclo.asesor);


            //CreditoIBSDAO.printValues(objlCredit);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objlCredit;
    }
    
    /*public boolean actualizaCreditoInterciclo(Connection con, CicloGrupalVO cicloVO, Vector notificaciones, double montoIncremento) throws Exception {

        boolean bandera = true;
        CreditoCartVO creditoVO = new CreditoCartVO();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        String strGarantia = "";
        double porGarantia = 0.00;
        try {
            strGarantia = new CatalogoDAO().getGarantia(cicloVO.getGarantia());
            porGarantia = Double.parseDouble(strGarantia);
            porGarantia = porGarantia / 100;
            creditoVO = creditoDAO.getCredito(cicloVO.getIdCreditoIBS());
            creditoVO.setMontoCredito(creditoVO.getMontoCredito() + montoIncremento);
            creditoVO.setMontoDesembolsado(creditoVO.getMontoDesembolsado() + montoIncremento);
            creditoVO.setValorCuota(cicloVO.tablaAmortInterciclo[5].montoPagar);
            creditoVO.setMontoCuentaCongelada(creditoVO.getMontoCuentaCongelada() + (montoIncremento * porGarantia));
            creditoDAO.updateCreditoInterciclo(con, creditoVO);
        } catch (Exception e) {
            bandera = false;
            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se logró registrar el CRÉDITO en el sistema de cartera IBS -- " + e.getMessage()));
            e.printStackTrace();
        }
        return bandera;
    }*/
    /*Metodo para actualizar saldo de fondeador cuando se aplica pago de pagare*/
    public static boolean actualizaSaldoFondeador(PagoPagareVO pago) throws Exception 
    {
        PagoPagareVO pagoPagare = new PagoPagareVO();
        PagoPagareDAO dao = new PagoPagareDAO();
        EventoHelper eventohelper = new EventoHelper();
        double saldo_nuevo = 0;
        int res = 0;
        boolean result = true;

        try 
        {
            //obtenemos el valor del pago capital
            //objlCredit = creditoDAO.getCreditoReferencia(pago.referencia);
            
            //obtenemos query el saldo del fondeador catalogo parametros
            
            //realizamos la operacion resta para disminuir saldo fondeador
            //monto_nuevo = objlCredit.getMontoCuenta() + pago.monto;
            //objlCredit.setMontoCuenta(monto_nuevo);
            
            //realizamos la disminucion del saldo en bd
            //eventohelper.registraPago(objlCredit, pago);
            //res = creditoDAO.updatePagoCredito(objlCredit);//ACTUALIZA EL MONTO DEL GRUPO	
        } catch (Exception e) {
            Logger.debug("Excepcion en actualizaSaldoCuenta : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());

        }

        return result;
    }
}
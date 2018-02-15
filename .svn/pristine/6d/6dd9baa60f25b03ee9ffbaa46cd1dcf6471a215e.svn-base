/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.dao.bsc;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.bsc.MigracionInfoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class MigracionInfoDAO extends DAOMaster {

    private Connection con = null;
    private Statement stm = null;
    private PreparedStatement pstm = null;
    private ResultSet res = null;
    private String query = "";

    public void MigracionDatosBSC() throws ClientesDBException, ClientesException {

        MigracionInfoVO periodo = getPeriodo();
        ArrayList<MigracionInfoVO> arrSaldoDash = new ArrayList<MigracionInfoVO>();
        double costoFin = ClientesConstants.COSTO_FINANCIERO/100;
        double totRecuperaTotal = 0;
        double totCapitalRecup = 0;
        double totCFCapiRecup = 0;
        double totThrouRecup = 0;
        double recuperaTotal = 0;
        double capitalRecup = 0;
        double cfCapiRecup = 0;
        double throuRecup = 0;
        double dineroDisper = 0;
        double totMontoRecuperar = 0;
        double totMontoRecuperado = 0;
        double montoRecuperar = 0;
        double montoRecuperado = 0;
        double totDDP = 0;
        double DDP = 0;
        double totMora = 0;
        double mora = 0;
        double totGruposMora = getTotalGruposMora(periodo, 0, 0);
        String nomCampo1 = "";
        String nomCampo2 = "";
        String nomCampo3 = "";
        String nomCampo4 = "";
        double numGrupos = 0;
        double totNumGrupos = 0;
        double numGruposReno = 0;
        double promPonderado = getPromedioPonderado(periodo, 0, 0);
        double disrecu = 0;
        double saldoCartera = 0;
        double totSaldoCartera = 0;
        int newSucursales = getNumeroSucursales(periodo, "abiertas");
        int closeSucursales = getNumeroSucursales(periodo, "cerradas");
        int totSucursales = getNumeroSucursales(periodo, "activas");        
        
        ArrayList<MigracionInfoVO> arrSaldos = getSaldosTotales(periodo);
        for (MigracionInfoVO saldos : arrSaldos) {
            
            recuperaTotal = saldos.getCapitalPagado()+saldos.getInteresPagado();
            //System.out.println("recuperaTotal: "+recuperaTotal);
            capitalRecup = saldos.getCapitalPagado();
            //System.out.println("capitalRecup: "+capitalRecup);
            cfCapiRecup = capitalRecup*costoFin;
            //System.out.println("cfCapiRecup: "+cfCapiRecup);
            throuRecup = recuperaTotal-capitalRecup-cfCapiRecup;
            //System.out.println("throuRecup: "+throuRecup);
            
            totRecuperaTotal += recuperaTotal;
            //System.out.println("totRecuperaTotal: "+totRecuperaTotal);
            totCapitalRecup += capitalRecup;
            //System.out.println("totCapitalRecup: "+totCapitalRecup);
            totCFCapiRecup += cfCapiRecup;
            //System.out.println("totCFCapiRecup: "+totCFCapiRecup);
            totThrouRecup += throuRecup;
            //System.out.println("totThrouRecup: "+totThrouRecup);
            dineroDisper += saldos.getCapital();
            //System.out.println("dineroDisper: "+dineroDisper);
            montoRecuperar = (saldos.getCapital()+saldos.getInteres()+saldos.getIvaInteres())-(saldos.getCapitalPagado()+saldos.getInteresPagado()+saldos.getIvaInteresPagado());
            //System.out.println("montoRecuperar: "+montoRecuperar);
            montoRecuperado = saldos.getCapitalPagado()+saldos.getInteresPagado()+saldos.getIvaInteresPagado();
            //System.out.println("montoRecuperado: "+montoRecuperado);
            if(montoRecuperar==0)
                DDP = (montoRecuperado/1)*100;
            else
                DDP = (montoRecuperado/montoRecuperar)*100;
            //System.out.println("DDP: "+DDP);
            DDP = FormatUtil.roundDecimal(DDP, 2);
            //System.out.println("DDP: "+DDP);
            totMontoRecuperar += montoRecuperar;
            //System.out.println("totMontoRecuperar: "+totMontoRecuperar);
            totMontoRecuperado += montoRecuperado;
            //System.out.println("totMontoRecuperado: "+totMontoRecuperado);
            mora = getGruposSaldoMora(periodo, saldos.getNumSucursal());
            //System.out.println("mora: "+mora);
            totMora += mora;
            //System.out.println("totMora: "+totMora);
            numGrupos = getTotalGrupos(periodo, 1, saldos.getNumSucursal());
            //System.out.println("numGrupos: "+numGrupos);
            totNumGrupos += numGrupos;
            //System.out.println("totNumGrupos: "+totNumGrupos);
            numGruposReno = getTotalGrupos(periodo, 2, saldos.getNumSucursal());
            //System.out.println("numGruposReno: "+numGruposReno);
            saldoCartera = getCarteraVigente(saldos.getNumSucursal());
            //System.out.println("saldoCartera: "+saldoCartera);
            totSaldoCartera += saldoCartera;
            //System.out.println("totSaldoCartera: "+totSaldoCartera);
            
            if(getTipoSucursal(saldos.getNumSucursal())){
                nomCampo1 = "nrtsemana";
                nomCampo2 = "ncrsemana";
                nomCampo3 = "ncfcrsemana";
                nomCampo4 = "nmtrsemana";
            } else {
                nomCampo1 = "6mrtsemana";
                nomCampo2 = "6mcrsemana";
                nomCampo3 = "6mcfcrsemana";
                nomCampo4 = "6mtrsemana";
            }
            
            arrSaldoDash.add(new MigracionInfoVO(saldos.getNumSucursal(), periodo.getPeriodo(), nomCampo1, recuperaTotal, "Recuperación Total"));
            arrSaldoDash.add(new MigracionInfoVO(saldos.getNumSucursal(), periodo.getPeriodo(), nomCampo2, capitalRecup, "Capital Recuperado"));
            arrSaldoDash.add(new MigracionInfoVO(saldos.getNumSucursal(), periodo.getPeriodo(), nomCampo3, cfCapiRecup, "Costo Financiero del Capital Recuperado"));
            arrSaldoDash.add(new MigracionInfoVO(saldos.getNumSucursal(), periodo.getPeriodo(), nomCampo4, throuRecup, "Throughput de la Recuperación (T)"));
            arrSaldoDash.add(new MigracionInfoVO(saldos.getNumSucursal(), periodo.getPeriodo(), "ddsemana", saldos.getCapital(), "Dinero Dispersado"));
            arrSaldoDash.add(new MigracionInfoVO(saldos.getNumSucursal(), periodo.getPeriodo(), "ngsemana", numGrupos, "Número de Grupos"));
            arrSaldoDash.add(new MigracionInfoVO(saldos.getNumSucursal(), periodo.getPeriodo(), "prsemana", numGruposReno, "Porcentaje de Renovación"));
            arrSaldoDash.add(new MigracionInfoVO(saldos.getNumSucursal(), periodo.getPeriodo(), "mrpsemana", montoRecuperar, "Monto Para Recuperar en el Periodo"));
            arrSaldoDash.add(new MigracionInfoVO(saldos.getNumSucursal(), periodo.getPeriodo(), "mrtsemana", montoRecuperado, "Monto Recuperado a Tiempo"));
            arrSaldoDash.add(new MigracionInfoVO(saldos.getNumSucursal(), periodo.getPeriodo(), "ddpsemana", DDP, "Desempeño de Recuperación (DDP)"));
            arrSaldoDash.add(new MigracionInfoVO(saldos.getNumSucursal(), periodo.getPeriodo(), "mtsemana", mora, "Mora Total (> 1 dia)"));
            arrSaldoDash.add(new MigracionInfoVO(saldos.getNumSucursal(), periodo.getPeriodo(), "ngmsemana", getTotalGruposMora(periodo, 1, saldos.getNumSucursal()), "Número de Grupos en Mora"));
            arrSaldoDash.add(new MigracionInfoVO(saldos.getNumSucursal(), periodo.getPeriodo(), "dcppsemana", getPromedioPonderado(periodo, 1, saldos.getNumSucursal()), "Dias de Credito (Promedio Ponderado)"));
            arrSaldoDash.add(new MigracionInfoVO(saldos.getNumSucursal(), periodo.getPeriodo(), "sacsemana", saldoCartera, "Cartera Vigente"));
        }
        
        totDDP = (totMontoRecuperado/totMontoRecuperar)*100;
        totDDP = FormatUtil.roundDecimal(totDDP, 2);
        if(totRecuperaTotal == 0)
            disrecu = dineroDisper/1;
        else
            disrecu = dineroDisper/totRecuperaTotal;
        
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "rtsemana", totRecuperaTotal, "Recuperación Total"));
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "crsemana", totCapitalRecup, "Capital Recuperado"));
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "cfcrsemana", totCFCapiRecup, "Costo Financiero del Capital Recuperado"));
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "trsemana", totThrouRecup, "Throughput de la Recuperación (T)"));
        
        //SE AGREGAN LOS TOTALES
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "ddsemana", dineroDisper, "Dinero Dispersado"));
        
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "drsemana", dineroDisper/totRecuperaTotal, "Disperción/Recuperacion"));
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "ngsemana", totNumGrupos, "Número de Grupos"));
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "prsemana", getTotalGrupos(periodo, 3, 0), "Porcentaje de Renovación"));
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "mrpsemana", totMontoRecuperar, "Monto Para Recuperar en el Periodo"));
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "mrtsemana", totMontoRecuperado, "Monto Recuperado a Tiempo"));
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "ddpsemana", totDDP, "Desempeño de Recuperación (DDP)"));
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "mtsemana", totMora, "Mora Total (> 1 dia)"));
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "ngmsemana", totGruposMora, "Número de Grupos en Mora"));
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "dcppsemana", promPonderado, "Dias de Credito (Promedio Ponderado)"));
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "sasemana", newSucursales, "Sucursales Abiertas"));
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "scsemana", closeSucursales, "Sucursales Cerradas"));
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "stsemana", totSucursales, "Sucursales Totales"));
        arrSaldoDash.add(new MigracionInfoVO(0, periodo.getPeriodo(), "sacsemana", totSaldoCartera, "Cartera Vigente"));
        
        /*for (MigracionInfoVO saldosnew : arrSaldoDash) {
            System.out.println(saldosnew.getDashsucursal()+" "+saldosnew.getDashSemana()+" "+saldosnew.getDashNomCampo()+" "+saldosnew.getDashValCampo()+" "+saldosnew.getDashDescrip());
        }*/
        //INSERTAMOS LOS VALORES EN BSC
        insertSaldosTotales(arrSaldoDash);
        
    }

    public MigracionInfoVO getPeriodo() throws ClientesDBException, ClientesException {

        MigracionInfoVO periodo = null;
        query = "SELECT pe_periodo,pe_fecha_inicial,pe_fecha_final FROM c_periodos_sem WHERE DATE_SUB(CURDATE(),INTERVAL 7 DAY) BETWEEN pe_fecha_inicial AND pe_fecha_final";

        try {
            //con = getBSCConnection();
            stm = con.createStatement();
            res = stm.executeQuery(query);
            Logger.debug("Ejecutando = "+query);
            while (res.next()) {
                periodo = new MigracionInfoVO(res.getInt("pe_periodo"), res.getDate("pe_fecha_inicial"), res.getDate("pe_fecha_final"));
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getPeriodo: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getPeriodo: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return periodo;
    }

    public ArrayList<MigracionInfoVO> getSaldosTotales(MigracionInfoVO fechas) throws ClientesDBException, ClientesException {

        ArrayList<MigracionInfoVO> arrSaldostotales = new ArrayList<MigracionInfoVO>();
        query = "SELECT cr_num_sucursal,SUM(ta_abono_capital) AS CAPITAL,SUM(ta_capital_pagado) AS CAPITAL_PAG, "
                + "SUM(ta_interes) AS INTERES,SUM(ta_iva_interes) AS IVA_INTERES,SUM(ta_interes_pagado) AS INTERES_PAG, "
                + "SUM(ta_iva_interes_pagado) AS IVA_INTERES_PAG,SUM(ta_multa) AS MULTA,SUM(ta_iva_multa) AS IVA_MULTA, "
                + "SUM(ta_multa_pagado) AS MULTA_PAG,SUM(ta_iva_multa_pagado) AS IVA_MULTA_PAG, "
                + "SUM(ta_monto_pagar) AS MONTO_PAGAR,SUM(ta_monto_pagado) AS MONTO_PAGADO "
                + "FROM d_credito, d_tabla_amortizacion "
                + "WHERE ta_numcliente=cr_num_cliente AND ta_numcredito=cr_num_credito "
                + "AND ta_fecha_pago BETWEEN ? AND ? GROUP BY cr_num_sucursal "
                + "ORDER BY cr_num_sucursal,cr_num_cliente";

        try {
            con = getCWConnection();
            pstm = con.prepareStatement(query);
            pstm.setDate(1, fechas.getFecInicial());
            pstm.setDate(2, fechas.getFecFinal());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros["+fechas.getFecInicial()+", "+fechas.getFecFinal()+"]");
            res = pstm.executeQuery();
            while (res.next()) {
                arrSaldostotales.add(new MigracionInfoVO(res.getInt("cr_num_sucursal"), res.getDouble("CAPITAL"), res.getDouble("CAPITAL_PAG"), res.getDouble("INTERES"), 
                        res.getDouble("IVA_INTERES"), res.getDouble("INTERES_PAG"), res.getDouble("IVA_INTERES_PAG"), res.getDouble("MULTA"), res.getDouble("IVA_MULTA"),
                        res.getDouble("MULTA_PAG"), res.getDouble("IVA_MULTA_PAG"), res.getDouble("MONTO_PAGAR"), res.getDouble("MONTO_PAGADO")));
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getSaldosTotales: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getSaldosTotales: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return arrSaldostotales;
    }
    
    public boolean getTipoSucursal (int numSucursal) throws ClientesDBException, ClientesException{
        
        boolean tipoNueva = false;
        query = "SELECT su_numsucursal FROM c_sucursales WHERE DATE_SUB(CURDATE(),INTERVAL 6 MONTH)>su_fecha_alta AND su_numsucursal=?";
        
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, numSucursal);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros["+numSucursal+"]");
            res = pstm.executeQuery();
            if(res.next())
                tipoNueva = true;
            
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getTipoSucursal: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getTipoSucursal: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return tipoNueva;
    }
    
    public double getTotalGrupos(MigracionInfoVO fechas, int tipo, int sucursal) throws ClientesDBException, ClientesException {

        double numGrupos = 0;
        double numGruposLiq = 0;
        double numGruposReno = 0;
        query = "SELECT count(cr_num_cliente) AS numgrupos FROM d_credito, d_tabla_amortizacion WHERE ta_numcliente=cr_num_cliente "
                + "AND ta_numcredito=cr_num_credito AND ta_fecha_pago BETWEEN ? AND ?";
        if(tipo == 1)
            query+=" AND cr_num_sucursal=?";
        if(tipo == 2)
            query+=" AND cr_num_sucursal=? AND cr_status=3";
        if(tipo == 3)
            query+=" AND cr_status=3";

        try {
            con = getCWConnection();
            pstm = con.prepareStatement(query);
            pstm.setDate(1, fechas.getFecInicial());
            pstm.setDate(2, fechas.getFecFinal());
            if(tipo == 1 || tipo == 2)
                pstm.setInt(3, sucursal);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametos["+fechas.getFecInicial()+", "+fechas.getFecFinal()+", "+sucursal+"]");
            res = pstm.executeQuery();
            while (res.next()) {                
                if(tipo == 0 || tipo == 1)
                    numGrupos = res.getInt("numgrupos");
                if(tipo > 1)
                    numGruposLiq = res.getInt("numgrupos");
            }
            
            if(tipo > 1){
                query = "SELECT count(cr_num_cliente) AS numgrupos FROM d_credito WHERE cr_fecha_desembolso BETWEEN ? AND ? AND cr_num_solicitud!=1";
                if(tipo == 2)
                    query += " AND cr_num_sucursal=?";
                pstm = con.prepareStatement(query);
                pstm.setDate(1, fechas.getFecInicial());
                pstm.setDate(2, fechas.getFecFinal());
                if(tipo == 2)
                    pstm.setInt(3, sucursal);
                Logger.debug("Ejecutando = "+query);
                Logger.debug("Parametos["+fechas.getFecInicial()+", "+fechas.getFecFinal()+", "+sucursal+"]");
                res = pstm.executeQuery();
                while (res.next()) {                
                    numGruposReno = res.getInt("numgrupos");
                }
                if(numGruposReno != 0){
                    if(numGruposLiq == 0) numGruposLiq=1;
                    numGrupos = (numGruposReno*100)/numGruposLiq;
                }
            }
            
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getTotalGrupos: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getTotalGrupos: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return numGrupos;
    }
    
    public int getTotalGruposMora(MigracionInfoVO fechas, int tipo, int grupo) throws ClientesDBException, ClientesException {

        int numGrupos = 0;
        query = "SELECT count(cr_num_cliente) AS numgrupos FROM d_credito, d_tabla_amortizacion WHERE ta_numcliente=cr_num_cliente "
                + "AND ta_numcredito=cr_num_credito AND ta_fecha_pago BETWEEN ? AND ? AND CR_STATUS=2";
        if(tipo == 1)
            query +=" AND cr_num_sucursal=?";

        try {
            con = getCWConnection();
            pstm = con.prepareStatement(query);
            pstm.setDate(1, fechas.getFecInicial());
            pstm.setDate(2, fechas.getFecFinal());
            if(tipo == 1)
                pstm.setInt(3, grupo);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametos["+fechas.getFecInicial()+", "+fechas.getFecFinal()+"]");
            res = pstm.executeQuery();
            while (res.next()) {                
                numGrupos = res.getInt("numgrupos");
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getTotalGruposMora: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getTotalGruposMora: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return numGrupos;
    }
    
    public int getGruposSaldoMora(MigracionInfoVO fechas, int sucursal) throws ClientesDBException, ClientesException {

        int saldo = 0;
        query = "SELECT (ta_abono_capital-ta_capital_pagado)+(ta_interes-ta_interes_pagado)+(ta_iva_interes-ta_iva_interes_pagado)+ (ta_multa-ta_multa_pagado)+(ta_iva_multa-ta_iva_multa_pagado) AS saldomora "
                + " FROM d_credito, d_tabla_amortizacion WHERE ta_numcliente=cr_num_cliente AND ta_numcredito=cr_num_credito AND ta_fecha_pago<=? AND ta_pagado!='S' AND cr_num_sucursal=?";

        try {
            con = getCWConnection();
            pstm = con.prepareStatement(query);
            pstm.setDate(1, fechas.getFecFinal());
            pstm.setInt(2, sucursal);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametos["+fechas.getFecFinal()+", "+sucursal+"]");
            res = pstm.executeQuery();
            while (res.next()) {                
                saldo = res.getInt("saldomora");
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getGruposSaldoMora: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getGruposSaldoMora: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return saldo;
    }
    
    public double  getPromedioPonderado(MigracionInfoVO fechas, int tipo, int grupo) throws ClientesDBException, ClientesException {

        double diasPonderados = 0;
        query = "SELECT SUM(ib_dias_transcurridos) AS numdias,COUNT(ib_numclientesicap) AS numgrupos FROM d_saldos, d_tabla_amortizacion WHERE ta_id_cliente=ib_numclientesicap "
                + "AND ta_id_solicitud=ib_numsolicitudsicap AND ta_fecha_pago BETWEEN ? AND ?";
        if(tipo == 1)
            query += " AND ib_numsucursal=?";

        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setDate(1, fechas.getFecInicial());
            pstm.setDate(2, fechas.getFecFinal());
            if(tipo == 1)
                pstm.setInt(3, grupo);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametos["+fechas.getFecInicial()+", "+fechas.getFecFinal()+", "+grupo+"]");
            res = pstm.executeQuery();
            while (res.next()) {                
                diasPonderados = res.getInt("numdias")/res.getInt("numgrupos");
                System.out.println("diasPonderados: "+diasPonderados);
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getPromedioPonderado: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getPromedioPonderado: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return diasPonderados;
    }    
    
    public int getNumeroSucursales(MigracionInfoVO fechas, String tipo) throws ClientesDBException, ClientesException {

        int numero = 0;
        query = "SELECT COUNT(su_numsucursal) as numsucursales FROM c_sucursales WHERE su_numsucursal NOT IN (0,33) ";
        if(tipo.equals("abiertas"))
            query += "AND su_fecha_alta BETWEEN ? AND ? AND su_estatus=1";
        if(tipo.equals("cerradas"))
            query += "AND su_fecha_cierre BETWEEN ? AND ? AND su_estatus=0";
        if(tipo.equals("activas"))
            query += "AND su_estatus=1";

        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            if(!tipo.equals("activas")){
                pstm.setDate(1, fechas.getFecInicial());
                pstm.setDate(2, fechas.getFecFinal());
            }                
            Logger.debug("Ejecutando = "+query);
            res = pstm.executeQuery();
            while (res.next()){
                numero = res.getInt("numsucursales");
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getNumeroSucursales: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getNumeroSucursales: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return numero;
    }
    
    public double getCarteraVigente(int sucursal) throws ClientesDBException, ClientesException {

        double saldo = 0;
        query = "SELECT SUM(ib_saldo_con_intereses_al_final) AS saldo FROM clientes.d_saldos WHERE ib_numsucursal=? AND ib_estatus NOT IN (3,4)";

        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, sucursal);
            Logger.debug("Ejecutando = "+query);
            res = pstm.executeQuery();
            while (res.next()){
                saldo = res.getDouble("saldo");
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getCarteraVigente: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getCarteraVigente: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return saldo;
    }
    
    public void insertSaldosTotales(ArrayList<MigracionInfoVO> arrSaldosDash) throws ClientesDBException, ClientesException {

        query = "INSERT INTO dashboard (id, sucursal, id_semana, nombre_campo, valor_campo, descripcion) "+
                "VALUES (0,?,?,?,?,?)";

        try {
            //con = getBSCConnection();
            pstm = con.prepareStatement(query);
            for (MigracionInfoVO migraSaldos : arrSaldosDash) {
                pstm.setInt(1, migraSaldos.getDashsucursal());
                pstm.setInt(2, migraSaldos.getDashSemana());
                pstm.setString(3, migraSaldos.getDashNomCampo());
                pstm.setDouble(4, migraSaldos.getDashValCampo());
                pstm.setString(5, migraSaldos.getDashDescrip());
                Logger.debug("Ejecutando = "+query);
                Logger.debug("Parametos["+migraSaldos.getDashsucursal()+", "+migraSaldos.getDashSemana()+", "+migraSaldos.getDashNomCampo()
                        +", "+migraSaldos.getDashValCampo()+", "+migraSaldos.getDashDescrip()+"]");
                
                pstm.executeUpdate();
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en insertSaldosTotales: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en insertSaldosTotales: " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }
}

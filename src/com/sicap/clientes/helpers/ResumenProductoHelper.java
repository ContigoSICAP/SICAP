package com.sicap.clientes.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sicap.clientes.vo.CuentaVO;
import com.sicap.clientes.vo.ResumenVO;

public class ResumenProductoHelper {

    List list = null;
    static ResumenVO AA;
    static ResumenVO AB;
    static ResumenVO AE;
    static ResumenVO AM;
    static ResumenVO AR;
    static ResumenVO AV;
    static ResumenVO BC;
    static ResumenVO BL;
    static ResumenVO BR;
    static ResumenVO CA;
    static ResumenVO CC;
    static ResumenVO CF;
    static ResumenVO CO;
    static ResumenVO CP;
    static ResumenVO ED;
    static ResumenVO EQ;
    static ResumenVO FF;
    static ResumenVO FI;
    static ResumenVO FT;
    static ResumenVO GS;
    static ResumenVO HB;
    static ResumenVO HE;
    static ResumenVO HV;
    static ResumenVO LC;
    static ResumenVO MC;
    static ResumenVO NC;
    static ResumenVO ND;
    static ResumenVO NG;
    static ResumenVO OT;
    static ResumenVO PB;
    static ResumenVO PC;
    static ResumenVO PE;
    static ResumenVO PG;
    static ResumenVO PM;
    static ResumenVO PP;
    static ResumenVO SH;
    static ResumenVO TC;
    static ResumenVO TD;
    static ResumenVO TG;
    static ResumenVO TS;
    static ResumenVO UI;
    static ResumenVO VR;
    private int totalCuentas;
    private double totalLimite;
    private double totalAprobado;
    private double totalActual;
    private double totalVencido;
    private int totalSemanal;
    private int totalQuincenal;
    private int totalMensual;

    public ResumenProductoHelper(List _list) {
        this.list = _list;
        initObject();
    }

    @SuppressWarnings("unchecked")
    public List doResumen() {

        List<ResumenVO> listResumen = null;
        CuentaVO cuenta = null;
        Iterator iterator = list.iterator();
        try {
            while (iterator.hasNext()) {
                
                cuenta = (CuentaVO) iterator.next();
                sumaTotales(cuenta);
                
                if (cuenta.getTipoCredito().equals("AA")) {
                    if (AA == null) {
                        AA = new ResumenVO("ARRENDAMIENTO AUTOMOTRIZ");
                    }
                    sumaTotales(AA, cuenta);
                } else if (cuenta.getTipoCredito().equals("AB")) {
                    if (AB == null) {
                        AB = new ResumenVO("AUTOMOTRIZ BANCARIO");
                    }
                    sumaTotales(AB, cuenta);
                } else if (cuenta.getTipoCredito().equals("AE")) {
                    if (AE == null) {
                        AE = new ResumenVO("FISICA ACTIVIDAD EMPRESARIAL");
                    }
                    sumaTotales(AE, cuenta);
                } else if (cuenta.getTipoCredito().equals("AM")) {
                    if (AM == null) {
                        AM = new ResumenVO("APARATOS/MUEBLES");
                    }
                    sumaTotales(AM, cuenta);
                } else if (cuenta.getTipoCredito().equals("AR")) {
                    if (AR == null) {
                        AR = new ResumenVO("ARRENDATARIO");
                    }
                    sumaTotales(AR, cuenta);
                } else if (cuenta.getTipoCredito().equals("AV")) {
                    if (AV == null) {
                        AV = new ResumenVO("AVIACION");
                    }
                    sumaTotales(AV, cuenta);
                } else if (cuenta.getTipoCredito().equals("BC")) {
                    if (BC == null) {
                        BC = new ResumenVO("BANCA COMUNAL");
                    }
                    sumaTotales(BC, cuenta);
                } else if (cuenta.getTipoCredito().equals("BL")) {
                    if (BL == null) {
                        BL = new ResumenVO("BOTE/LANCHA");
                    }
                    sumaTotales(BL, cuenta);
                } else if (cuenta.getTipoCredito().equals("BR")) {
                    if (BR == null) {
                        BR = new ResumenVO("BIENES RAICES");
                    }
                    sumaTotales(BR, cuenta);
                } else if (cuenta.getTipoCredito().equals("CA")) {
                    if (CA == null) {
                        CA = new ResumenVO("COMPRA DE AUTOMÓVIL");
                    }
                    sumaTotales(CA, cuenta);
                } else if (cuenta.getTipoCredito().equals("CC")) {
                    if (CC == null) {
                        CC = new ResumenVO("CREDITO AL CONSUMO");
                    }
                    sumaTotales(CC, cuenta);
                } else if (cuenta.getTipoCredito().equals("CF")) {
                    if (CF == null) {
                        CF = new ResumenVO("CREDITO FISCAL");
                    }
                    sumaTotales(CF, cuenta);
                } else if (cuenta.getTipoCredito().equals("CO")) {
                    if (CO == null) {
                        CO = new ResumenVO("CONSOLIDACION");
                    }
                    sumaTotales(CO, cuenta);
                } else if (cuenta.getTipoCredito().equals("CP")) {
                    if (CP == null) {
                        CP = new ResumenVO("CREDITO PERSONAL AL CONSUMO");
                    }
                    sumaTotales(CP, cuenta);
                } else if (cuenta.getTipoCredito().equals("ED")) {
                    if (ED == null) {
                        ED = new ResumenVO("EDITORIAL");
                    }
                    sumaTotales(ED, cuenta);
                } else if (cuenta.getTipoCredito().equals("EQ")) {
                    if (EQ == null) {
                        EQ = new ResumenVO("EQUIPO");
                    }
                    sumaTotales(EQ, cuenta);
                } else if (cuenta.getTipoCredito().equals("FF")) {
                    if (FF == null) {
                        FF = new ResumenVO("FONDEO FIRA");
                    }
                    sumaTotales(FF, cuenta);
                } else if (cuenta.getTipoCredito().equals("FI")) {
                    if (FI == null) {
                        FI = new ResumenVO("FIANZA");
                    }
                    sumaTotales(FI, cuenta);
                } else if (cuenta.getTipoCredito().equals("FT")) {
                    if (FT == null) {
                        FT = new ResumenVO("FACTORAJE");
                    }
                    sumaTotales(FT, cuenta);
                } else if (cuenta.getTipoCredito().equals("GS")) {
                    if (GS == null) {
                        GS = new ResumenVO("GRUPO SOLIDARIO");
                    }
                    sumaTotales(GS, cuenta);
                } else if (cuenta.getTipoCredito().equals("HB")) {
                    if (HB == null) {
                        HB = new ResumenVO("HIPOTECARIO BANCARIO");
                    }
                    sumaTotales(HB, cuenta);
                } else if (cuenta.getTipoCredito().equals("HE")) {
                    if (HE == null) {
                        HE = new ResumenVO("PRESTAMO TIPO HOME EQUITY");
                    }
                    sumaTotales(HE, cuenta);
                } else if (cuenta.getTipoCredito().equals("HV")) {
                    if (HV == null) {
                        HV = new ResumenVO("HIPOTECARIO O VIVIENDA");
                    }
                    sumaTotales(HV, cuenta);
                } else if (cuenta.getTipoCredito().equals("LC")) {
                    if (LC == null) {
                        LC = new ResumenVO("LINEA DE CREDITO");
                    }
                    sumaTotales(LC, cuenta);
                } else if (cuenta.getTipoCredito().equals("MC")) {
                    if (MC == null) {
                        MC = new ResumenVO("MEJORA A LA CASA");
                    }
                    sumaTotales(MC, cuenta);
                } else if (cuenta.getTipoCredito().equals("NC")) {
                    if (NC == null) {
                        NC = new ResumenVO("DESCONOCIDO");
                    }
                    sumaTotales(NC, cuenta);
                } else if (cuenta.getTipoCredito().equals("ND")) {
                    if (ND == null) {
                        ND = new ResumenVO("NO DISPONIBLE");
                    }
                    sumaTotales(ND, cuenta);
                } else if (cuenta.getTipoCredito().equals("NG")) {
                    if (NG == null) {
                        NG = new ResumenVO("PRESTAMO NO GARANTIZADO");
                    }
                    sumaTotales(NG, cuenta);
                } else if (cuenta.getTipoCredito().equals("OT")) {
                    if (OT == null) {
                        OT = new ResumenVO("OTROS (MULTIPLES CREDITOS)");
                    }
                    sumaTotales(OT, cuenta);
                } else if (cuenta.getTipoCredito().equals("PB")) {
                    if (PB == null) {
                        PB = new ResumenVO("PRESTAMO PERSONAL BANCARIO");
                    }
                    sumaTotales(PB, cuenta);
                } else if (cuenta.getTipoCredito().equals("PC")) {
                    if (PC == null) {
                        PC = new ResumenVO("PROCAMPO");
                    }
                    sumaTotales(PC, cuenta);
                } else if (cuenta.getTipoCredito().equals("PE")) {
                    if (PE == null) {
                        PE = new ResumenVO("PRESTAMO PARA ESTUDIANTE");
                    }
                    sumaTotales(PE, cuenta);
                } else if (cuenta.getTipoCredito().equals("PG")) {
                    if (PG == null) {
                        PG = new ResumenVO("PRESTAMO GARANTIZADO");
                    }
                    sumaTotales(PG, cuenta);
                } else if (cuenta.getTipoCredito().equals("PM")) {
                    if (PM == null) {
                        PM = new ResumenVO("PRESTAMO EMPRESARIAL");
                    }
                    sumaTotales(PM, cuenta);
                } else if (cuenta.getTipoCredito().equals("PP")) {
                    if (PP == null) {
                        PP = new ResumenVO("PRESTAMO PERSONAL");
                    }
                    sumaTotales(PP, cuenta);
                } else if (cuenta.getTipoCredito().equals("SH")) {
                    if (SH == null) {
                        SH = new ResumenVO("SEGUNDA HIPOTECA");
                    }
                    sumaTotales(SH, cuenta);
                } else if (cuenta.getTipoCredito().equals("TC")) {
                    if (TC == null) {
                        TC = new ResumenVO("TARJETA DE CREDITO");
                    }
                    sumaTotales(TC, cuenta);
                } else if (cuenta.getTipoCredito().equals("TD")) {
                    if (TD == null) {
                        TD = new ResumenVO("TARJETA DEPARTAMENTAL");
                    }
                    sumaTotales(TD, cuenta);
                } else if (cuenta.getTipoCredito().equals("TG")) {
                    if (TG == null) {
                        TG = new ResumenVO("TARJETA GARANTIZADA");
                    }
                    sumaTotales(TG, cuenta);
                } else if (cuenta.getTipoCredito().equals("TS")) {
                    if (TS == null) {
                        TS = new ResumenVO("TARJETA DE SERVICIOS");
                    }
                    sumaTotales(TS, cuenta);
                } else if (cuenta.getTipoCredito().equals("UI")) {
                    if (UI == null) {
                        UI = new ResumenVO("USO INTERNO");
                    }
                    sumaTotales(UI, cuenta);
                } else if (cuenta.getTipoCredito().equals("VR")) {
                    if (VR == null) {
                        VR = new ResumenVO("VEHICULO RECREATIVO");
                    }
                    sumaTotales(VR, cuenta);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        listResumen = doList();
        if (!listResumen.isEmpty()) {
            listResumen.add(addTotales());
        }

        return listResumen;

    }

    private void sumaTotales(CuentaVO cuenta) {
        totalCuentas++;
        totalLimite += cuenta.getLimiteCredito();
        totalAprobado += cuenta.getCreditoMaximo();
        totalActual += cuenta.getSaldoActual();
        totalVencido += cuenta.getSaldoVencido();
    }

    private ResumenVO addTotales() {
        ResumenVO totales = new ResumenVO("TOTALES");

        totales.setTotalCuentas(totalCuentas);
        totales.setLimite(totalLimite);
        totales.setAprobado(totalAprobado);
        totales.setActual(totalActual);
        totales.setVencido(totalVencido);
        totales.setPagoSemanal(totalSemanal);
        totales.setPagoQuincenal(totalQuincenal);
        totales.setPagoMensual(totalMensual);

        return totales;
    }

    protected void sumaTotales(ResumenVO producto, CuentaVO cuenta) {

        double aux = 0;
        int auxInt = 0;
        int cont = 0;

        cont = producto.getTotalCuentas() + 1;
        producto.setTotalCuentas(cont);

        aux = producto.getLimite() + cuenta.getLimiteCredito();
        producto.setLimite(aux);

        aux = producto.getAprobado() + cuenta.getCreditoMaximo();
        producto.setAprobado(aux);

        aux = producto.getActual() + cuenta.getSaldoActual();
        producto.setActual(aux);

        aux = producto.getVencido() + cuenta.getSaldoVencido();
        producto.setVencido(aux);
        
        if (cuenta.getFrecuencia().equals("SEMANAL")) {
            auxInt = producto.getPagoSemanal() + cuenta.getAPagar();
            producto.setPagoSemanal(auxInt);
            totalSemanal += cuenta.getAPagar();
        } else if (cuenta.getFrecuencia().equals("QUINCENAL")) {
            auxInt = producto.getPagoQuincenal() + cuenta.getAPagar();
            producto.setPagoQuincenal(auxInt);
            totalQuincenal += cuenta.getAPagar();
        } else if (cuenta.getFrecuencia().equals("MENSUAL")) {
            auxInt = producto.getPagoMensual() + cuenta.getAPagar();
            producto.setPagoMensual(auxInt);
            totalMensual += cuenta.getAPagar();
        }

    }

    protected List doList() {

        List<ResumenVO> list = new ArrayList<ResumenVO>();

        if (AA != null) {
            list.add(AA);
        }
        if (AB != null) {
            list.add(AB);
        }
        if (AE != null) {
            list.add(AE);
        }
        if (AM != null) {
            list.add(AM);
        }
        if (AR != null) {
            list.add(AR);
        }
        if (AV != null) {
            list.add(AV);
        }
        if (BC != null) {
            list.add(BC);
        }
        if (BL != null) {
            list.add(BL);
        }
        if (BR != null) {
            list.add(BR);
        }
        if (CA != null) {
            list.add(CA);
        }
        if (CC != null) {
            list.add(CC);
        }
        if (CF != null) {
            list.add(CF);
        }
        if (CO != null) {
            list.add(CO);
        }
        if (CP != null) {
            list.add(CP);
        }
        if (ED != null) {
            list.add(ED);
        }
        if (EQ != null) {
            list.add(EQ);
        }
        if (FF != null) {
            list.add(FF);
        }
        if (FI != null) {
            list.add(FI);
        }
        if (FT != null) {
            list.add(FT);
        }
        if (GS != null) {
            list.add(GS);
        }
        if (HB != null) {
            list.add(HB);
        }
        if (HE != null) {
            list.add(HE);
        }
        if (HV != null) {
            list.add(HV);
        }
        if (LC != null) {
            list.add(LC);
        }
        if (MC != null) {
            list.add(MC);
        }
        if (NC != null) {
            list.add(NC);
        }
        if (ND != null) {
            list.add(ND);
        }
        if (NG != null) {
            list.add(NG);
        }
        if (OT != null) {
            list.add(OT);
        }
        if (PB != null) {
            list.add(PB);
        }
        if (PC != null) {
            list.add(PC);
        }
        if (PE != null) {
            list.add(PE);
        }
        if (PG != null) {
            list.add(PG);
        }
        if (PM != null) {
            list.add(PM);
        }
        if (PP != null) {
            list.add(PP);
        }
        if (SH != null) {
            list.add(SH);
        }
        if (TC != null) {
            list.add(TC);
        }
        if (TD != null) {
            list.add(TD);
        }
        if (TG != null) {
            list.add(TG);
        }
        if (TS != null) {
            list.add(TS);
        }
        if (UI != null) {
            list.add(UI);
        }
        if (VR != null) {
            list.add(VR);
        }

        return list;

    }

    private void initObject() {
        totalCuentas = 0;
        totalLimite = 0;
        totalAprobado = 0;
        totalActual = 0;
        totalVencido = 0;
        totalSemanal = 0;
        totalQuincenal = 0;
        totalMensual = 0;

        AA = null;
        AB = null;
        AE = null;
        AM = null;
        AR = null;
        AV = null;
        BC = null;
        BL = null;
        BR = null;
        CA = null;
        CC = null;
        CF = null;
        CO = null;
        CP = null;
        ED = null;
        EQ = null;
        FF = null;
        FI = null;
        FT = null;
        GS = null;
        HB = null;
        HE = null;
        HV = null;
        LC = null;
        MC = null;
        NC = null;
        ND = null;
        NG = null;
        OT = null;
        PB = null;
        PC = null;
        PE = null;
        PG = null;
        PM = null;
        PP = null;
        SH = null;
        TC = null;
        TD = null;
        TG = null;
        TS = null;
        UI = null;
        VR = null;
    }
    /*
    private static void printCuentas(Cuenta cuenta){
    Logger.debug("\n==============================================");
    Logger.debug("credito : "+  cuenta.getDescTipoCredito());
    Logger.debug("credito : "  +  cuenta.getTipoCredito());
    Logger.debug("limite: "    +  cuenta.getLimiteCredito()+"");
    Logger.debug("aprobado: "  +  cuenta.getCreditoMaximo()+"");
    Logger.debug("actual: "    +  cuenta.getSaldoActual()+"");
    Logger.debug("vencido: "   +  cuenta.getSaldoVencido()+"");
    Logger.debug("aPagar: "    +  cuenta.getAPagar()+"");
    Logger.debug("==============================================");
    }
     */
}

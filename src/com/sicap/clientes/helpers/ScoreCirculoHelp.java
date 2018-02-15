package com.sicap.clientes.helpers;

public class ScoreCirculoHelp {
    
    public String getRazonScore(String tipo){
        
        String razon = "";
        if ( tipo==null )
            return razon;
        if( tipo.substring(0, 1).equals("A")){
            if(tipo.equals("A0"))
                razon = "Historial de pago de la cuenta es demasiado nuevo";
            else if(tipo.equals("A1"))
                razon = "Último reporte de la Cuenta en estatus de morosidad";
            else if(tipo.equals("A2"))
                razon = "Monto de crédito disponibles en cuentas revolventes";
            else if(tipo.equals("A3"))
                razon = "Monto de deuda en cuentas es demasiado alto";
            else if(tipo.equals("A4"))
                razon = "Monto de deuda en el banco/nacional en cuentas revolventes";
            else if(tipo.equals("A5"))
                razon = "Monto de deuda clasificado en cobranza";
            else if(tipo.equals("A6"))
                razon = "Monto de deuda en las cuentas morosas";
            else if(tipo.equals("A7"))
                razon = "Monto de deuda en cuentas abiertas recientemente es demasiado alto";
            else if(tipo.equals("A8"))
                razon = "Monto de deuda en cuentas revolventes abiertas recientemente de banco/nacionales es muy alta";
            else if(tipo.equals("A9"))
                razon = "Monto de deuda en cuentas abiertos recientemente en empresas de financiamiento al consumo es demasiado alto";
        } else if(tipo.substring(0, 1).equals("B")){
            if(tipo.equals("B0"))
                razon = "Monto de deuda en cuentas de minoristas abiertas recientemente es demasiado alta";
            else if(tipo.equals("B1"))
                razon = "Monto de deuda en cuentas revolventes abiertas recientemente es demasiado alto";
            else if(tipo.equals("B2"))
                razon = "Monto de deuda en cuentas de empresas de financiamiento de ventas abiertas recientemente es muy alto";
            else if(tipo.equals("B3"))
                razon = "Monto de deuda en las cuentas de minoristas";
            else if(tipo.equals("B4"))
                razon = "Monto de deuda en cuentas revolventes";
            else if(tipo.equals("B5"))
                razon = "Monto de deuda en cuentas de crédito revolventes es demasiado alto";
            else if(tipo.equals("B6"))
                razon = "Montos en mora en cuentas";
        } else if(tipo.substring(0, 1).equals("D")){
            if(tipo.equals("D0"))
                razon = "Declaración de quiebra reportada";
            else if(tipo.equals("D1"))
                razon = "Fecha de la última consulta muy reciente";
            else if(tipo.equals("D2"))
                razon = "Morosidad en las cuentas";
            else if(tipo.equals("D3"))
                razon = "Morosidad en cuentas abiertas recientemente";
            else if(tipo.equals("D4"))
                razon = "Eliminación de registro público o la cobranza reportada";
            else if(tipo.equals("D5"))
                razon = "La frecuencia de la morosidad";
            else if(tipo.equals("D6"))
                razon = "Nivel de morosidad en las cuentas";
            else if(tipo.equals("D7"))
                razon = "Morosidad grave";
            else if(tipo.equals("D8"))
                razon = "Morosidad grave, y registro público, o la cobranza reportada";
            else if(tipo.equals("D9"))
                razon = "Morosidad grave, registro público eliminado, o la cobranza reportada";
        } else if(tipo.substring(0, 1).equals("E")){
            if(tipo.equals("E0"))
                razon = "Información Demográfica";
            else if(tipo.equals("E1"))
                razon = "Falta información demográfica";
            else if(tipo.equals("E2"))
                razon = "Número de consultas";
            else if(tipo.equals("E3"))
                razon = "Frecuencia de las consultas";
            else if(tipo.equals("E4"))
                razon = "La falta de información reciente de la cuenta";
            else if(tipo.equals("E5"))
                razon = "Monto de deuda en los registros públicos eliminados";
            else if(tipo.equals("E6"))
                razon = "Proporción del saldo sobre el límite de las cuentas";
        } else if(tipo.substring(0, 1).equals("F")){
            if(tipo.equals("F0"))
                razon = "Historial de pagos disponible es insuficiente";
            else if(tipo.equals("F1"))
                razon = "Falta de cuentas de crédito de reciente creación";
            else if(tipo.equals("F2"))
                razon = "Falta de cuentas revolventes de reciente creación";
            else if(tipo.equals("F3"))
                razon = "Falta de información reciente de préstamo de autofinanciamiento";
            else if(tipo.equals("F4"))
                razon = "Falta de información reciente préstamo de auto";
            else if(tipo.equals("F5"))
                razon = "Falta de información reciente de banco/nacional revolvente";
            else if(tipo.equals("F6"))
                razon = "Falta de información reciente sobre cuentas de empresas de financiamiento al consumo";
            else if(tipo.equals("F7"))
                razon = "Falta de información reciente de préstamos";
            else if(tipo.equals("F8"))
                razon = "Falta de información reciente de préstamos hipotecarios";
            else if(tipo.equals("F9"))
                razon = "Falta de información reciente de préstamos no hipotecarios";
        } else if(tipo.substring(0, 1).equals("G")){
            if(tipo.equals("G0"))
                razon = "Falta de información reciente de cuentas de minoristas";
            else if(tipo.equals("G1"))
                razon = "Falta de información reciente de cuentas revolventes";
            else if(tipo.equals("G2"))
                razon = "No hay créditos hipotecarios reportados";
            else if(tipo.equals("G3"))
                razon = "No hay información de saldos revolventes recientes de Bancos/Nacional";
            else if(tipo.equals("G4"))
                razon = "No hay información de saldos no hipotecarios recientes";
            else if(tipo.equals("G5"))
                razon = "No hay saldos de cuentas minoristas";
            else if(tipo.equals("G6"))
                razon = "No hay saldos revolventes recientes";
        } else if(tipo.substring(0, 1).equals("J")){
            if(tipo.equals("J0"))
                razon = "Tiempo de Duración de las cuentas se han establecido";
            else if(tipo.equals("J1"))
                razon = "Tiempo de Duración de las cuentas de automóviles se han establecido";
            else if(tipo.equals("J2"))
                razon = "Tiempo de Duración de las cuentas revolventes del banco/nacionales se han establecido";
            else if(tipo.equals("J3"))
                razon = "Tiempo de Duración de los préstamos de compañías de financiamiento al consumo se han establecido";
            else if(tipo.equals("J4"))
                razon = "Tiempo de Duración de los préstamos a plazos se han establecido";
            else if(tipo.equals("J5"))
                razon = "Tiempo de Duración de las cuentas hipotecarias reportado se han establecido";
            else if(tipo.equals("J6"))
                razon = "Tiempo de Duración de los préstamos a plazos abiertos se han establecido";
            else if(tipo.equals("J7"))
                razon = "Tiempo de Duración de las cuentas de minoristas se han establecido";
            else if(tipo.equals("J8"))
                razon = "Tiempo de Duración de cuentas revolventes se han establecido";
            else if(tipo.equals("J9"))
                razon = "Tiempo transcurrido desde la actividad de la cuenta es demasiado largo";
        } else if(tipo.substring(0, 1).equals("K0")){
            if(tipo.equals("K0"))
                razon = "Tiempo transcurrido desde la Morosidad es demasiado reciente o desconocida";
            else if(tipo.equals("K1"))
                razon = "Tiempo transcurrido desde la eliminación del registro público o de la cobranza es demasiado corto";
            else if(tipo.equals("K2"))
                razon = "Tiempo transcurrido desde la apertura de la cuenta más reciente es demasiado corto";
            else if(tipo.equals("K3"))
                razon = "Tiempo transcurrido desde la más reciente apertura de cuenta automática es demasiado corto";
            else if(tipo.equals("K4"))
                razon = "Tiempo transcurrido desde la apertura de cuenta revolvente de banco / nacionales más reciente es demasiado corto";
            else if(tipo.equals("K5"))
                razon = "Tiempo transcurrido desde la apertura de cuenta de empresas de financiamiento de consumo más reciente es demasiado corto";
            else if(tipo.equals("K6"))
                razon = "Tiempo transcurrido desde la apertura de cuenta de préstamo a plazos más reciente es demasiado corto";
            else if(tipo.equals("K7"))
                razon = "Tiempo transcurrido desde la apertura de cuenta minorista más recientes";
            else if(tipo.equals("K8"))
                razon = "Tiempo transcurrido desde que se abrió la cuenta revolvente más reciente";
            else if(tipo.equals("K9"))
                razon = "Ha transcurrido muy poco tiempo desde que se abrió la cuenta más reciente de 'sales finance'";
        } else if(tipo.substring(0, 1).equals("M")){
            if(tipo.equals("M0"))
                razon = "Tiene un número de cuentas actualmente en impago";
            else if(tipo.equals("M1"))
                razon = "Número de cuentas 'marcadas' con impago";
            else if(tipo.equals("M2"))
                razon = "Número de cuentas con impago reciente";
            else if(tipo.equals("M3"))
                razon = "Número de cuentas revolventes bancarias/nacionales activas";
            else if(tipo.equals("M4"))
                razon = "Número de cuentas activas de 'retail'";
            else if(tipo.equals("M5"))
                razon = "Número de registros públicos adversos/derogados";
            else if(tipo.equals("M6"))
                razon = "Número de cuentas revolventes bancarias/nacionales con saldo";
            else if(tipo.equals("M7"))
                razon = "Número de cuentas revolventes bancarias/nacionales";
            else if(tipo.equals("M8"))
                razon = "Número de cuentas revolventes bancarias/nacionales u otras cuentas revolventes";
            else if(tipo.equals("M9"))
                razon = "Número de cobranzas registradas";
        } else if(tipo.substring(0, 1).equals("N")){
            if(tipo.equals("N0"))
                razon = "Número de cuentas de 'consumer finance company' establecidas relativas a la longitud de la historia del consumidor";
            else if(tipo.equals("N1"))
                razon = "Número de consultas por compañías de financiamiento al consumo";
            else if(tipo.equals("N2"))
                razon = "Número de cuentas establecidas";
            else if(tipo.equals("N3"))
                razon = "Número de 'open installment loans'";
            else if(tipo.equals("N4"))
                razon = "Número de cuentas recientemente abiertas con compañías de financiamiento al consumo";
            else if(tipo.equals("N5"))
                razon = "Número de cuentas de retail";
            else if(tipo.equals("N6"))
                razon = "Número de cuentas de 'retail' con saldo";
            else if(tipo.equals("N7"))
                razon = "Número de cuentas revolventes";
            else if(tipo.equals("N8"))
                razon = "Número de cuentas revolventes con saldos más altos que su límite";
        } else if(tipo.substring(0, 1).equals("P")){
            if(tipo.equals("P0"))
                razon = "La proporción del saldo vs límite en las cuentas de préstamo automotriz es muy alta";
            else if(tipo.equals("P1"))
                razon = "La proporción del saldo vs límite en las cuentas con impago es muy alta";
            else if(tipo.equals("P2"))
                razon = "La proporción del saldo vs límite en las cuentas de financiamiento al consumo es muy alta";
            else if(tipo.equals("P3"))
                razon = "La proporción del saldo vs límite en las cuentas de 'retail' es muy alta";
            else if(tipo.equals("P5"))
                razon = "La proporción del saldo vs el límite de crédito en cuentas revolventes nacionales o en otras cuentas revolventes es muy alta";
            else if(tipo.equals("P6"))
                razon = "La proporción del saldo vs límites de crédito en cuentas revolventes es muy alta";
            else if(tipo.equals("P7"))
                razon = "La proporción del saldo vs límite en 'sales finance company' es muy alta";
            else if(tipo.equals("P8"))
                razon = "La proporción del saldo vs el préstamo en créditos hipotecarios es muy alta";
            else if(tipo.equals("P9"))
                razon = "La proporción de saldo del préstamo vs la cantidad del préstamo es muy alta";
        } else if(tipo.substring(0, 1).equals("Q")){
            if(tipo.equals("Q0"))
                razon = "La proporción de saldos revolventes vs saldos totales es muy alta";
            else if(tipo.equals("Q1"))
                razon = "La proporción de saldos vs límites de créditos en las cuentas revolventes bancarias/nacionales es muy alta";
        } else if(tipo.substring(0, 1).equals("R")){
            if(tipo.equals("R0"))
                razon = "Muy pocas cuentas han sido pagadas actualmente como fue convenido";
            else if(tipo.equals("R1"))
                razon = "Muy pocas cuentas con saldos";
            else if(tipo.equals("R2"))
                razon = "Muy pocas cuentas con información reciente de pagos";
            else if(tipo.equals("R3"))
                razon = "Muy pocas cuentas activas";
            else if(tipo.equals("R4"))
                razon = "Muy pocas cuentas revolventes bancarias/nacionales";
            else if(tipo.equals("R5"))
                razon = "Muy pocas cuentas revolventes bancarias/nacionales con información reciente de pagos";
            else if(tipo.equals("R6"))
                razon = "Muy pocas cuentas con compañías de financiamiento al consumo con información reciente de pagos";
            else if(tipo.equals("R7"))
                razon = "Muy pocas cuentas de 'instalment'";
            else if(tipo.equals("R8"))
                razon = "Muy pocas cuentas de 'retail'";
            else if(tipo.equals("R9"))
                razon = "Muy pocas cuentas de 'retail' con información reciente de pagos";
        } else if(tipo.substring(0, 1).equals("S")){
            if(tipo.equals("S0"))
                razon = "Muy pocas cuentas revolventes";
            else if(tipo.equals("S1"))
                razon = "Muy pocas cuentas revolventes con información reciente de pagos";
            else if(tipo.equals("S2"))
                razon = "Muy pocas cuentas de 'sales finance company' con información reciente de pagos";
        } else if(tipo.substring(0, 1).equals("T")){
            if(tipo.equals("T0"))
                razon = "Muchas cuentas abiertas recientemente";
            else if(tipo.equals("T1"))
                razon = "Muchas cuentas con saldos";
            else if(tipo.equals("T2"))
                razon = "Muchas cuentas revolventes bancarias/nacionales";
            else if(tipo.equals("T3"))
                razon = "Muchas cuentas de compañías financiamiento al consumo";
            else if(tipo.equals("T4"))
                razon = "Muchas cuentas de 'instalment'";
            else if(tipo.equals("T5"))
                razon = "Muchas consultas en los últimos 12 meses";
            else if(tipo.equals("T6"))
                razon = "Muchas cuentas activas recientemente";
            else if(tipo.equals("T7"))
                razon = "Muchas cuentas de automóvil activas recientemente";
            else if(tipo.equals("T8"))
                razon = "Muchas cuentas revolventes bancarias/nacionales activas recientemente";
            else if(tipo.equals("T9"))
                razon = "Muchas cuentas de compañías financiamiento al consumo recientemente activas";
        } else if(tipo.substring(0, 1).equals("U")){
            if(tipo.equals("U0"))
                razon = "Muchas cuentas de 'instalment loan' recientemente activas";
            else if(tipo.equals("U1"))
                razon = "Muchas cuentas de 'retail' recientemente activas";
            else if(tipo.equals("U2"))
                razon = "Muchas cuentas de 'sales finance company' recientemente activas";
            else if(tipo.equals("U4"))
                razon = "Muchas cuentas recientemente abiertas con saldos";
            else if(tipo.equals("U5"))
                razon = "Muchas cuentas revolventes bancarias/nacionales recientemente abiertas";
            else if(tipo.equals("U6"))
                razon = "Muchas cuentas de compañías de financiamiento al consumo recientemente abiertas";
            else if(tipo.equals("U7"))
                razon = "Muchas cuentas de 'instalment' recientemente abiertas";
            else if(tipo.equals("U8"))
                razon = "Muchas cuentas de 'retail' recientemente abiertas con saldos";
            else if(tipo.equals("U9"))
                razon = "Muchas cuentas revolventes recientemente abiertas";
        } else if(tipo.substring(0, 1).equals("V")){
            if(tipo.equals("V0"))
                razon = "Muchas cuentas revolventes recientemente abiertas con saldos";
            else if(tipo.equals("V1"))
                razon = "Muchas cuentas de 'sales finance company' recientemente abiertas";
            else if(tipo.equals("V2"))
                razon = "Muchas cuentas de 'retail'";
            else if(tipo.equals("V3"))
                razon = "Muchas cuentas revolventes";
            else if(tipo.equals("V4"))
                razon = "Muchas cuentas revolventes bancarias/nacionales recientemente abiertas con saldos";
        } else if(tipo.substring(0, 1).equals("W")){
            if(tipo.equals("W3"))
                razon = "Número de cuentas 'subprime'";
            else if(tipo.equals("W4"))
                razon = "Falta de información de cargo reciente a tarjeta";
            else if(tipo.equals("W5"))
                razon = "Sin saldos de cargo a tarjeta recientes";
            else if(tipo.equals("W6"))
                razon = "Porporción de saldos vs límites de crédito en cuentas de 'tarjetas de cargo' es muy alta";
            else if(tipo.equals("W7"))
                razon = "Sin saldos de cuenta recientes";
            else if(tipo.equals("W8"))
                razon = "La proporción de 'Cash Advances' vs el 'Límite de Cash Advance' es muy alta";
            else if(tipo.equals("W9"))
                razon = "Cash Advance Activity on Accounts";
        } else if(tipo.substring(0, 1).equals("X")){
            if(tipo.equals("X0"))
                razon = "Pagos pendientes en las cuentas";
        } else if(tipo.substring(0, 1).equals("C")){
            if(tipo.equals("C1"))
                razon = "No existe el expediente";
            else if(tipo.equals("C2"))
                razon = "El expediente encontrado no tiene cuentas";
            else if(tipo.equals("C3"))
                razon = "El expediente encontrado está marcado como fallecido";
            else if(tipo.equals("C4"))
                razon = "El expediente encontrado no tiene cuantas activas en los últimos 24 meses";
            else if(tipo.equals("C5"))
                razon = "El expediente esta marcado como fraudulento o sospechoso de fraude";
        } else{
            if(tipo.equals("SC"))
                razon = "No pudo calcularse score";
        }
        return razon;
    }
    
}

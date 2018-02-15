package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;

public class BitacoraSalidaCarteraVO implements Serializable {
    private int numGrupo;
    private int numCiclo;
    private int fondeadorOrigen;
    private int fondeadorDestino;
    private Date fechaSalida;
    private int estatusCartera;
    private String usuario;

    public BitacoraSalidaCarteraVO() {
        numGrupo = 0;
        numCiclo = 0;
        fondeadorOrigen = 0;
        fondeadorDestino = 0;
        fechaSalida = null;
    }
    
    public BitacoraSalidaCarteraVO(int numGrupo, int numCiclo, int fondeadorOrigen, int fondeadorDestino, Date fechaSalida) {
        this.numGrupo = numGrupo;
        this.numCiclo = numCiclo;
        this.fondeadorOrigen = fondeadorOrigen;
        this.fondeadorDestino = fondeadorDestino;
        this.fechaSalida = fechaSalida;
    }
    
    
    public int getNumGrupo() {
        return numGrupo;
    }

    public void setNumGrupo(int numGrupo) {
        this.numGrupo = numGrupo;
    }

    public int getNumCiclo() {
        return numCiclo;
    }

    public void setNumCiclo(int numCiclo) {
        this.numCiclo = numCiclo;
    }

    public int getFondeadorOrigen() {
        return fondeadorOrigen;
    }

    public void setFondeadorOrigen(int fondeadorOrigen) {
        this.fondeadorOrigen = fondeadorOrigen;
    }

    public int getFondeadorDestino() {
        return fondeadorDestino;
    }

    public void setFondeadorDestino(int fondeadorDestino) {
        this.fondeadorDestino = fondeadorDestino;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
    
    public int getEstatusCartera() {
        return estatusCartera;
    }

    public void setEstatusCartera(int estatusCartera) {
        this.estatusCartera = estatusCartera;
    }
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    
    
}

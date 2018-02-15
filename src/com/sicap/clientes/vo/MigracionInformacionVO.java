package com.sicap.clientes.vo;

import java.io.Serializable;

public class MigracionInformacionVO implements Serializable{
    
    public int id;
    public String campo;

    public MigracionInformacionVO(int id, String campo) {
        this.id = id;
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class ClienteVO implements Serializable {

    public int idCliente;
    public int idClienteIBS;
    public int idSucursal;
    public int idBanco;
    public int estatus;
    public String nombre;
    public String aPaterno;
    public String aMaterno;
    public String nombreCompleto;
    public String rfc;
    public Date fechaNacimiento;
    public int entidadNacimiento;
    public int sexo;
    public int nacionalidad;
    public int tipoIdentificacion;
    public String numeroIdentificacion;
    public int estadoCivil;
    public int estadoCivilFommur;
    public String correoElectronico;
    public DireccionVO direcciones[];
    public SolicitudVO solicitudes[];
    public ConyugeVO conyuge;
    public Timestamp fechaCaptura;
    public int dependientesEconomicos;
    public int idGrupo;
    public int nivelEstudios;
    public int LenguaIndigena;
    public int Discapacidad;
    public int UsodeInternet;
    public int RedesSociales;
    public String curp;
    
    public int sucursal;
    public String sucursalSAP;
    public String calle;
    public String numext;
    public String numint;
    public String estado;
    public String municipio;
    public String colonia;
    public String situacionViv;
    public int antiguedadViv;
    public String telefono;
    public String tipoDoc;
    public String tipoSexo;
    public String tipoNacion;
    public String tipoEdoCivil;
    public int idMigracion;
    public int origenMigracion;
    public SolicitudVO ultimaSolicitud;
    public String mensaje;

    public ClienteVO() {

        idCliente = 0;
        idClienteIBS = 0;
        idSucursal = 0;
        idBanco = 0;
        estatus = 0;
        nombre = null;
        aPaterno = null;
        aMaterno = null;
        nombreCompleto = null;
        rfc = null;
        entidadNacimiento = 0;
        fechaNacimiento = null;
        sexo = 0;
        nacionalidad = 0;
        tipoIdentificacion = 0;
        numeroIdentificacion = null;
        estadoCivil = 0;
        correoElectronico = null;
        direcciones = null;
        solicitudes = null;
        conyuge = null;
        fechaCaptura = null;
        dependientesEconomicos = 0;
        idGrupo = 0;
        idGrupo = 0;
        curp = "";
        nivelEstudios=0;
        LenguaIndigena =0;
        UsodeInternet=0;
        RedesSociales=0;
        Discapacidad=0;
        ultimaSolicitud = null;
        mensaje = "";
        
    }

    public String toString() {
        String respuesta = null;
        respuesta = "idCliente=[" + idCliente + "],";
        respuesta = "idClienteIBS=[" + idClienteIBS + "],";
        respuesta += "idSucursal=[" + idSucursal + "],";
        respuesta += "idBanco=[" + idBanco + "],";
        respuesta += "estatus=[" + estatus + "],";
        respuesta += "nombre=[" + nombre + "],";
        respuesta += "aPaterno=[" + aPaterno + "],";
        respuesta += "aMaterno=[" + aMaterno + "],";
        respuesta += "nombreCompleto=[" + nombreCompleto + "],";
        respuesta += "rfc=[" + rfc + "],";
        respuesta += "entidadNacimiento=[" + entidadNacimiento + "],";
        respuesta += "fechaNacimiento=[" + fechaNacimiento + "],";
        respuesta += "sexo=[" + sexo + "],";
        respuesta += "nacionalidad=[" + nacionalidad + "],";
        respuesta += "tipoIdentificacion=[" + tipoIdentificacion + "],";
        respuesta += "numeroIdentificacion=[" + numeroIdentificacion + "],";
        respuesta += "estadoCivil=[" + estadoCivil + "],";
        respuesta += "correoElectronico=[" + correoElectronico + "],";
        respuesta += "dependientesEconomicos=[" + dependientesEconomicos + "]";
        respuesta += "idGrupo=[" + idGrupo + "]";
        respuesta += "nivelEstudios=[" + nivelEstudios + "]";
        respuesta += "CURP=[" + curp + "]";
        respuesta += "nivelEstudios=[" + nivelEstudios + "]";
        respuesta += "LenguaIndigena=[" + LenguaIndigena + "]";
        respuesta += "UsodeInternet=[" + UsodeInternet + "]";
        respuesta += "Redes_sociales=[" + RedesSociales + "]";
        respuesta += "Discapacidad=[" + Discapacidad + "]";
        
        return respuesta;
    }



    public ClienteVO(int idCliente, String nombre, String aPaterno, String aMaterno, String rfc, Date fechaNacimiento) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.aPaterno = aPaterno;
        this.aMaterno = aMaterno;
        this.rfc = rfc;
        this.fechaNacimiento = fechaNacimiento;
    }

    public ClienteVO(int idCliente, String nombre, String aPaterno, String aMaterno, String rfc, Date fechaNacimiento, String numeroIdentificacion, int dependientesEconomicos, int sucursal, String calle, String numext, String numint, String estado, String municipio, String colonia, String situacionViv, int antiguedadViv, String telefono, String tipoDoc, String tipoSexo, String tipoNacion, String tipoEdoCivil) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.aPaterno = aPaterno;
        this.aMaterno = aMaterno;
        this.rfc = rfc;
        this.fechaNacimiento = fechaNacimiento;
        this.numeroIdentificacion = numeroIdentificacion;
        this.dependientesEconomicos = dependientesEconomicos;
        this.sucursal = sucursal;
        this.calle = calle;
        this.numext = numext;
        this.numint = numint;
        this.estado = estado;
        this.municipio = municipio;
        this.colonia = colonia;
        this.situacionViv = situacionViv;
        this.antiguedadViv = antiguedadViv;
        this.telefono = telefono;
        this.tipoDoc = tipoDoc;
        this.tipoSexo = tipoSexo;
        this.tipoNacion = tipoNacion;
        this.tipoEdoCivil = tipoEdoCivil;
    }

    public String getaMaterno() {
        return aMaterno;
    }

    public void setaMaterno(String aMaterno) {
        this.aMaterno = aMaterno;
    }

    public String getaPaterno() {
        return aPaterno;
    }

    public void setaPaterno(String aPaterno) {
        this.aPaterno = aPaterno;
    }

    public ConyugeVO getConyuge() {
        return conyuge;
    }

    public void setConyuge(ConyugeVO conyuge) {
        this.conyuge = conyuge;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public int getDependientesEconomicos() {
        return dependientesEconomicos;
    }

    public void setDependientesEconomicos(int dependientesEconomicos) {
        this.dependientesEconomicos = dependientesEconomicos;
    }

    public DireccionVO[] getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(DireccionVO[] direcciones) {
        this.direcciones = direcciones;
    }

    public int getEntidadNacimiento() {
        return entidadNacimiento;
    }

    public void setEntidadNacimiento(int entidadNacimiento) {
        this.entidadNacimiento = entidadNacimiento;
    }

    public int getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(int estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public Timestamp getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Timestamp fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdClienteIBS() {
        return idClienteIBS;
    }

    public void setIdClienteIBS(int idClienteIBS) {
        this.idClienteIBS = idClienteIBS;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public int getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(int nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public int getNivelEstudios() {
        return nivelEstudios;
    }

    public void setNivelEstudios(int nivelEstudios) {
        this.nivelEstudios = nivelEstudios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public SolicitudVO[] getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(SolicitudVO[] solicitudes) {
        this.solicitudes = solicitudes;
    }

    public int getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(int tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public int getSucursal() {
        return sucursal;
    }

    public void setSucursal(int sucursal) {
        this.sucursal = sucursal;
    }

    public int getAntiguedadViv() {
        return antiguedadViv;
    }

    public void setAntiguedadViv(int antiguedadViv) {
        this.antiguedadViv = antiguedadViv;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getNumext() {
        return numext;
    }

    public void setNumext(String numext) {
        this.numext = numext;
    }

    public String getNumint() {
        return numint;
    }

    public void setNumint(String numint) {
        this.numint = numint;
    }

    public String getSituacionViv() {
        return situacionViv;
    }

    public void setSituacionViv(String situacionViv) {
        this.situacionViv = situacionViv;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getTipoEdoCivil() {
        return tipoEdoCivil;
    }

    public void setTipoEdoCivil(String tipoEdoCivil) {
        this.tipoEdoCivil = tipoEdoCivil;
    }

    public String getTipoNacion() {
        return tipoNacion;
    }

    public void setTipoNacion(String tipoNacion) {
        this.tipoNacion = tipoNacion;
    }

    public String getTipoSexo() {
        return tipoSexo;
    }

    public void setTipoSexo(String tipoSexo) {
        this.tipoSexo = tipoSexo;
    }

    public int getOrigenMigracion() {
        return origenMigracion;
    }

    public void setOrigenMigracion(int origenMigracion) {
        this.origenMigracion = origenMigracion;
    }

    public int getIdMigracion() {
        return idMigracion;
    }

    public void setIdMigracion(int idMigracion) {
        this.idMigracion = idMigracion;
    }
        public int getLenguaIndigena() {
        return LenguaIndigena;
    }

    public void setLenguaIndigena(int LenguaIndigena) {
        this.LenguaIndigena = LenguaIndigena;
    }

    public int getDiscapacidad() {
        return Discapacidad;
    }

    public void setDiscapacidad(int Discapacidad) {
        this.Discapacidad = Discapacidad;
    }

    public int getUsodeInternet() {
        return UsodeInternet;
    }

    public void setUsodeInternet(int UsodeInternet) {
        this.UsodeInternet = UsodeInternet;
    }

    public int getRedesSociales() {
        return RedesSociales;
    }

    public void setRedesSociales(int Redes_sociales) {
        this.RedesSociales = Redes_sociales;
    }

    public SolicitudVO getUltimaSolicitud() {
        return ultimaSolicitud;
    }

    public void setUltimaSolicitud(SolicitudVO ultimaSolicitud) {
        this.ultimaSolicitud = ultimaSolicitud;
    }

    public int getEstadoCivilFommur() {
        return estadoCivilFommur;
    }

    public void setEstadoCivilFommur(int estadoCivilFommur) {
        this.estadoCivilFommur = estadoCivilFommur;
    }

    public String getSucursalSAP() {
        return sucursalSAP;
    }

    public void setSucursalSAP(String sucursalSAP) {
        this.sucursalSAP = sucursalSAP;
    }
    
    
}
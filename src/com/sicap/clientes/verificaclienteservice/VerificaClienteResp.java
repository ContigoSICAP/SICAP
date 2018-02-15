/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.verificaclienteservice;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author LDAVILA
 */
public class VerificaClienteResp {

    private String status = null;
    private String errorCode;
    private String errorDetail;
    private Date timeStamp;

    //Datos Cliente
    private int idClienteSicap;
    private String sucursal;
    private String fechaCaptura;
    private int dependientesEconomicos;
    private int idGrupo;
    private int idCiclo;
    private int nivelEstudios;
    private String LenguaIndigena;
    private String Discapacidad;
    private String UsodeInternet;
    private String RedesSociales;
    private String curp; 
    private String nombre;
    private String aPaterno;
    private String aMaterno;
    private String rfc;
    private String fechaNacimiento;
    private int entidadNacimiento;
    private String sexo;
    private int nacionalidad;
    private int tipoIdentificacion;
    private String numeroIdentificacion;
    private int estadoCivil;
    private String correoElectronico;
    private String firmaElectronica;
    private int rolHogar;
    private String otroCredito;
    private String mejorIngreso;
    private String tieneProgProspera;
    private String idProgProspera;
    //Datos Direcion
    private int estado;
    private int numMunicipio;
    private String ciudad;
    private String idColonia;
    private String calle;
    private String numeroExterior;
    private String numeroInterior;
    private String cp;
    private String asentamiento_cp;
    private String antDomicilio;
    private int idLocalidad;
    private int tipoVialidad;
    private int tipoAsentamiento;
    private int situacionViv;
    private String antiguedadViv;
    private String entreCalle;
    //Datos Telefono
    private String numeroTelefono;
    private String numeroCelular;
    //
    private int Numcreditos;
    private double MontoMaximo;
    private double UltimoMonto;    
    private String FechaUltimoMovimiento;
    private String FechaDesembolso;
    
    private String FechaActDocs;
    
    private String nomUltGrupo;
    private int estatusUltGrupo;
    private int semTransUltGrupo;
    private int numCuotas;
    
    public VerificaClienteResp(){
    status = null;
    errorCode = "";
    errorDetail= "";
    idClienteSicap = 0;
    sucursal = "";
    fechaCaptura = "";;
    dependientesEconomicos= 0;
    idGrupo= 0;
    idCiclo = 0;
    nivelEstudios  = 0;
    LenguaIndigena= "";
    Discapacidad= "";
    UsodeInternet= "";
    RedesSociales= "";
    curp= "";
    nombre= "";
    aPaterno= "";
    aMaterno= "";
    rfc= "";
    fechaNacimiento= "";   
    entidadNacimiento = 0;
    sexo="";
    nacionalidad =0;
    tipoIdentificacion=0;
    numeroIdentificacion= "";
    estadoCivil =0;
    correoElectronico= "";
    firmaElectronica= "";
    rolHogar=0;
    otroCredito= "";
    mejorIngreso= "";
    tieneProgProspera= "";
    idProgProspera= "";
    //Datos Direcion
    estado=0;
    numMunicipio=0;
    ciudad= "";
    idColonia= "";
    calle= "";
    numeroExterior= "";
    numeroInterior= "";
    cp= "";
    asentamiento_cp= "";
    antDomicilio= "";
    idLocalidad=0;
    tipoVialidad=0;
    tipoAsentamiento=0;
    situacionViv=0;
    antiguedadViv= "";
    entreCalle= "";    
    numeroTelefono= "";
    numeroCelular= "";    
    Numcreditos=0;
    MontoMaximo=0;
    UltimoMonto=0;    
    FechaUltimoMovimiento= "";
    FechaDesembolso= "";
    FechaActDocs= "";
    nomUltGrupo = "";
    estatusUltGrupo=0;
    semTransUltGrupo=0;
    numCuotas = 0;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getIdClienteSicap() {
        return idClienteSicap;
    }

    public void setIdClienteSicap(int idClienteSicap) {
        this.idClienteSicap = idClienteSicap;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public int getDependientesEconomicos() {
        return dependientesEconomicos;
    }

    public void setDependientesEconomicos(int dependientesEconomicos) {
        this.dependientesEconomicos = dependientesEconomicos;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public int getNivelEstudios() {
        return nivelEstudios;
    }

    public void setNivelEstudios(int nivelEstudios) {
        this.nivelEstudios = nivelEstudios;
    }

    public String getLenguaIndigena() {
        return LenguaIndigena;
    }

    public void setLenguaIndigena(String LenguaIndigena) {
        this.LenguaIndigena = LenguaIndigena;
    }

    public String getDiscapacidad() {
        return Discapacidad;
    }

    public void setDiscapacidad(String Discapacidad) {
        this.Discapacidad = Discapacidad;
    }

    public String getUsodeInternet() {
        return UsodeInternet;
    }

    public void setUsodeInternet(String UsodeInternet) {
        this.UsodeInternet = UsodeInternet;
    }

    public String getRedesSociales() {
        return RedesSociales;
    }

    public void setRedesSociales(String RedesSociales) {
        this.RedesSociales = RedesSociales;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getaPaterno() {
        return aPaterno;
    }

    public void setaPaterno(String aPaterno) {
        this.aPaterno = aPaterno;
    }

    public String getaMaterno() {
        return aMaterno;
    }

    public void setaMaterno(String aMaterno) {
        this.aMaterno = aMaterno;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEntidadNacimiento() {
        return entidadNacimiento;
    }

    public void setEntidadNacimiento(int entidadNacimiento) {
        this.entidadNacimiento = entidadNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(int nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public int getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(int tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public int getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(int estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(String firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getNumMunicipio() {
        return numMunicipio;
    }

    public void setNumMunicipio(int numMunicipio) {
        this.numMunicipio = numMunicipio;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getIdColonia() {
        return idColonia;
    }

    public void setIdColonia(String idColonia) {
        this.idColonia = idColonia;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeroExterior() {
        return numeroExterior;
    }

    public void setNumeroExterior(String numeroExterior) {
        this.numeroExterior = numeroExterior;
    }

    public String getNumeroInterior() {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        this.numeroInterior = numeroInterior;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getAsentamiento_cp() {
        return asentamiento_cp;
    }

    public void setAsentamiento_cp(String asentamiento_cp) {
        this.asentamiento_cp = asentamiento_cp;
    }

    public String getAntDomicilio() {
        return antDomicilio;
    }

    public void setAntDomicilio(String antDomicilio) {
        this.antDomicilio = antDomicilio;
    }

    public int getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(int idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public int getTipoVialidad() {
        return tipoVialidad;
    }

    public void setTipoVialidad(int tipoVialidad) {
        this.tipoVialidad = tipoVialidad;
    }

    public int getTipoAsentamiento() {
        return tipoAsentamiento;
    }

    public void setTipoAsentamiento(int tipoAsentamiento) {
        this.tipoAsentamiento = tipoAsentamiento;
    }

    public int getSituacionViv() {
        return situacionViv;
    }

    public void setSituacionViv(int situacionViv) {
        this.situacionViv = situacionViv;
    }

    public String getAntiguedadViv() {
        return antiguedadViv;
    }

    public void setAntiguedadViv(String antiguedadViv) {
        this.antiguedadViv = antiguedadViv;
    }

    public String getEntreCalle() {
        return entreCalle;
    }

    public void setEntreCalle(String entreCalle) {
        this.entreCalle = entreCalle;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public int getNumcreditos() {
        return Numcreditos;
    }

    public void setNumcreditos(int Numcreditos) {
        this.Numcreditos = Numcreditos;
    }

    public double getMontoMaximo() {
        return MontoMaximo;
    }

    public void setMontoMaximo(double MontoMaximo) {
        this.MontoMaximo = MontoMaximo;
    }

    public double getUltimoMonto() {
        return UltimoMonto;
    }

    public void setUltimoMonto(double UltimoMonto) {
        this.UltimoMonto = UltimoMonto;
    }

    public int getRolHogar() {
        return rolHogar;
    }

    public void setRolHogar(int rolHogar) {
        this.rolHogar = rolHogar;
    }

    public String getOtroCredito() {
        return otroCredito;
    }

    public void setOtroCredito(String otroCredito) {
        this.otroCredito = otroCredito;
    }

    public String getMejorIngreso() {
        return mejorIngreso;
    }

    public void setMejorIngreso(String mejorIngreso) {
        this.mejorIngreso = mejorIngreso;
    }

    public String getTieneProgProspera() {
        return tieneProgProspera;
    }

    public void setTieneProgProspera(String tieneProgProspera) {
        this.tieneProgProspera = tieneProgProspera;
    }

    public String getIdProgProspera() {
        return idProgProspera;
    }

    public void setIdProgProspera(String idProgProspera) {
        this.idProgProspera = idProgProspera;
    } 

    public String getFechaActDocs() {
        return FechaActDocs;
    }

    public void setFechaActDocs(String FechaActDocs) {
        this.FechaActDocs = FechaActDocs;
    }    

    public String getNomUltGrupo() {
        return nomUltGrupo;
    }

    public void setNomUltGrupo(String nomUltGrupo) {
        this.nomUltGrupo = nomUltGrupo;
    }

    public int getEstatusUltGrupo() {
        return estatusUltGrupo;
    }

    public void setEstatusUltGrupo(int estatusUltGrupo) {
        this.estatusUltGrupo = estatusUltGrupo;
    }   

    public String getFechaUltimoMovimiento() {
        return FechaUltimoMovimiento;
    }

    public void setFechaUltimoMovimiento(String FechaUltimoMovimiento) {
        this.FechaUltimoMovimiento = FechaUltimoMovimiento;
    }

    public String getFechaDesembolso() {
        return FechaDesembolso;
    }

    public void setFechaDesembolso(String FechaDesembolso) {
        this.FechaDesembolso = FechaDesembolso;
    }

    public int getSemTransUltGrupo() {
        return semTransUltGrupo;
    }

    public void setSemTransUltGrupo(int semTransUltGrupo) {
        this.semTransUltGrupo = semTransUltGrupo;
    }

    public int getNumCuotas() {
        return numCuotas;
    }

    public void setNumCuotas(int numCuotas) {
        this.numCuotas = numCuotas;
    }
    
}

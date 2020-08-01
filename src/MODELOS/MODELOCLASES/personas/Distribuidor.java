/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.MODELOCLASES.personas;

import MODELOS.MODELOCLASES.personas.datos.Direccion;
import MODELOS.MODELOCLASES.personas.datos.Telefono;
import java.util.Date;

/**
 *
 * @author Daniel Huenul
 */
public class Distribuidor {
    private int id;
    private String rut;
    private String nombre_empresa;
    private Date anno_asociadad;
    private Telefono telefono;
    private Direccion direccion;

    public Distribuidor() {
    }

    public Distribuidor(int id, String rut, String nombre_empresa, Date anno_asociadad, Telefono telefono, Direccion direccion) {
        this.id = id;
        this.rut = rut;
        this.nombre_empresa = nombre_empresa;
        this.anno_asociadad = anno_asociadad;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre_empresa() {
        return nombre_empresa;
    }

    public void setNombre_empresa(String nombre_empresa) {
        this.nombre_empresa = nombre_empresa;
    }

    public Date getAnno_asociadad() {
        return anno_asociadad;
    }

    public void setAnno_asociadad(Date anno_asociadad) {
        this.anno_asociadad = anno_asociadad;
    }

    public Telefono getTelefono() {
        return telefono;
    }

    public void setTelefono(Telefono telefono) {
        this.telefono = telefono;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return rut ;
    } 
    
    public String mostrarDatos() {
        return "Distribuidor{" + "id=" + id + ", rut=" + rut + ", nombre_empresa=" + nombre_empresa + ", anno_asociadad=" + anno_asociadad + ", telefono=[" +telefono.getId()+ "-"+telefono.getNumero()+"], direccion=[" + direccion.getId() + "-"+direccion.getDireccion()+"]";
    }
    
    
    
}

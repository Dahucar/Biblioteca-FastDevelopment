/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.MODELOCLASES.personas;

/**
 *
 * @author Daniel Huenul
 */
public class Usuario {
    private int id;
    private String rut;
    private String nombre;
    private String apellido_p;
    private String apellido_m;

    public Usuario() {
    }

    public Usuario(int id, String rut, String nombre, String apellido_p, String apellido_m) {
        this.id = id;
        this.rut = rut;
        this.nombre = nombre;
        this.apellido_p = apellido_p;
        this.apellido_m = apellido_m;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido_p() {
        return apellido_p;
    }

    public void setApellido_p(String apellido_p) {
        this.apellido_p = apellido_p;
    }

    public String getApellido_m() {
        return apellido_m;
    }

    public void setApellido_m(String apellido_m) {
        this.apellido_m = apellido_m;
    }

    @Override
    public String toString() {
        return this.nombre + " " + this.apellido_m + " " + this.apellido_m;
    }  
    
    public String mostrarDatos() {
        return "id=" + id + ", rut=" + rut + ", nombre=" + nombre + ", apellido_p=" + apellido_p + ", apellido_m=" + apellido_m + "} ";
    }
    
    
    
}

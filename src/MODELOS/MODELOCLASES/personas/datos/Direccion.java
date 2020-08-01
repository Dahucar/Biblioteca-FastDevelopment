/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.MODELOCLASES.personas.datos;

/**
 *
 * @author Daniel Huenul
 */
public class Direccion {
    private int id;
    private String direccion;

    public Direccion() {
    }

    public Direccion(int id, String direccion) {
        this.id = id;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String numero) {
        this.direccion = numero;
    }

    @Override
    public String toString() {
        return direccion;
    }
    
    
}

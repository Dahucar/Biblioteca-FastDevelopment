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
public class Trabajador extends Usuario {

    public Trabajador() {
        
    }

    public Trabajador(int id, String rut, String nombre, String apellido_p, String apellido_m) {
        super(id, rut, nombre, apellido_p, apellido_m);
    }
    
    @Override
    public String toString() {
        return super.getNombre() + " " + super.getApellido_p();
    }
    
    @Override
    public String mostrarDatos() {
        return "Trabajador. " + super.mostrarDatos();
    }
}

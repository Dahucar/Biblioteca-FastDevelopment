/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.MODELOCLASES.personas;

import java.util.Date;

/**
 *
 * @author Daniel Huenul
 */
public class Cliente extends Usuario{
    private Date anno_sociedad;

    public Cliente() {
    }

    public Cliente(Date anno_sociedad) {
        this.anno_sociedad = anno_sociedad;
    }

    public Cliente(Date anno_sociedad, int id, String rut, String nombre, String apellido_p, String apellido_m) {
        super(id, rut, nombre, apellido_p, apellido_m);
        this.anno_sociedad = anno_sociedad;
    }

    public Date getAnno_sociedad() {
        return anno_sociedad;
    }

    public void setAnno_sociedad(Date anno_sociedad) {
        this.anno_sociedad = anno_sociedad;
    }

    @Override
    public String toString() {
        return super.getNombre() + " " + super.getApellido_p();
    }
    
    @Override
    public String mostrarDatos(){
        return "Cliente. "+super.mostrarDatos() +", anno_sociedad=" +this.anno_sociedad;
    }
    
}

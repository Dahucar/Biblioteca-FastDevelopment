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
public class Correo {
    private int id;
    private String correo;
    private String contrasenna;

    public Correo() {
    }

    public Correo(int id, String correo, String contrasenna) {
        this.id = id;
        this.correo = correo;
        this.contrasenna = contrasenna;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenna() {
        return contrasenna;
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }

    @Override
    public String toString() {
        return this.correo;
    }
    
    
}

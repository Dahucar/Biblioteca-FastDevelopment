/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.MODELOCLASES.libros;

import MODELOS.MODELOCLASES.libros.Libro;

/**
 *
 * @author Daniel Huenul
 */
public class Idioma {
    private int id;
    private String idioma;
    private Libro idlibro;

    public Idioma() { 
    }

    public Idioma(int id, String idioma, Libro idlibro) {
        this.id = id;
        this.idioma = idioma;
        this.idlibro = idlibro; 
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Libro getIdlibro() {
        return idlibro;
    }

    public void setIdlibro(Libro idlibro) {
        this.idlibro = idlibro;
    }

    @Override
    public String toString() {
        return "Idioma{" + "id=" + id + ", idioma=" + idioma + ", idlibro=" + idlibro + '}';
    }
    
    
    
}

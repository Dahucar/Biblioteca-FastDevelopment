/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.MODELOCLASES.libros;

import MODELOS.MODELOCLASES.libros.Estado;
import MODELOS.MODELOCLASES.libros.Editorial;

/**
 *
 * @author Daniel Huenul
 */
public class Libro {
    private int id;
    private int numSerie;
    private int isbn;
    private String nombre;
    private int numPaginas;
    private float precio;
    private int stock;
    private Editorial editorial;
    private Estado estado;

    public Libro(int id, int numSerie, int isbn, String nombre, int numPaginas, float precio, int stock, Editorial editorial, Estado estado) {
        this.id = id;
        this.numSerie = numSerie;
        this.isbn = isbn;
        this.nombre = nombre;
        this.numPaginas = numPaginas;
        this.precio = precio;
        this.stock = stock;
        this.editorial = editorial;
        this.estado = estado;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Libro() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(int numSerie) {
        this.numSerie = numSerie;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumPaginas() {
        return numPaginas;
    }

    public void setNumPaginas(int numPaginas) {
        this.numPaginas = numPaginas;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return this.nombre;
    }
 
    public String mostrarDatos() {
        return "Libro{" + "id=" + id + ", numSerie=" + numSerie + ", isbn=" + isbn + ", nombre=" + nombre + ", numPaginas=" + numPaginas + ", precio=" + precio + ", stock="+stock+", editorial=" + editorial + ", estado=" + estado + '}';
    }

    
    
    
}

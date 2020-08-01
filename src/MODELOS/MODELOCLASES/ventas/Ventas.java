/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.MODELOCLASES.ventas;

import MODELOS.MODELOCLASES.personas.Cliente;
import MODELOS.MODELOCLASES.personas.Trabajador;

/**
 *
 * @author Daniel Huenul
 */
public class Ventas {
    private int id;
    private String codigo_venta;
    private Cliente cliente;
    private Trabajador trabajador;
    private Boletas boleta;

    public Ventas() {
    }

    public Ventas(int id, String codigo_venta, Cliente cliente, Trabajador trabajador, Boletas boleta) {
        this.id = id;
        this.codigo_venta = codigo_venta;
        this.cliente = cliente;
        this.trabajador = trabajador;
        this.boleta = boleta;
    } 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo_venta() {
        return codigo_venta;
    }

    public void setCodigo_venta(String codigo_venta) {
        this.codigo_venta = codigo_venta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public Boletas getBoleta() {
        return boleta;
    }

    public void setBoleta(Boletas boleta) {
        this.boleta = boleta;
    }

    @Override
    public String toString() {
        return "Ventas{" + "id=" + id + ", codigoVenta="+codigo_venta+",[cliente=" + cliente.mostrarDatos() + "], [trabajador=" + trabajador.mostrarDatos() + "],  [boleta=" + boleta.toString() + ']';
    }
    
    
}

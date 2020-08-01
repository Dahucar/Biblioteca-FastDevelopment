/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.MODELOCLASES.ventas;

import java.util.Date;

/**
 *
 * @author Daniel Huenul
 */
public class Boletas {
    private int id;
    private int folio;
    private float precio_neto;
    private float costo_iva;
    private float precio_iva;
    private String fecha_venta;
    private MetodoPago metodo_pago;

    public Boletas() {
    }

    public Boletas(int id, int folio, float precio_neto, float costo_iva, float precio_iva, String fecha_venta, MetodoPago metodo_pago) {
        this.id = id;
        this.folio = folio;
        this.precio_neto = precio_neto;
        this.costo_iva = costo_iva;
        this.precio_iva = precio_iva;
        this.fecha_venta = fecha_venta;
        this.metodo_pago = metodo_pago;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public float getPrecio_neto() {
        return precio_neto;
    }

    public void setPrecio_neto(float precio_neto) {
        this.precio_neto = precio_neto;
    }

    public float getCosto_iva() {
        return costo_iva;
    }

    public void setCosto_iva(float costo_iva) {
        this.costo_iva = costo_iva;
    }

    public float getPrecio_iva() {
        return precio_iva;
    }

    public void setPrecio_iva(float precio_iva) {
        this.precio_iva = precio_iva;
    }

    public String getFecha_venta() {
        return fecha_venta;
    }

    public void setFecha_venta(String fecha_venta) {
        this.fecha_venta = fecha_venta;
    }

    public MetodoPago getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(MetodoPago metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    @Override
    public String toString() {
        return folio +" -> "+ fecha_venta;
    }
    
    
}

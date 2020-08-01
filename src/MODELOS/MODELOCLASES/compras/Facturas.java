/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.MODELOCLASES.compras;

import MODELOS.MODELOCLASES.ventas.MetodoPago;
import java.util.Date;

/**
 *
 * @author Daniel Huenul
 */
public class Facturas {
    private int id;
    private int folio;
    private float precio_neto;
    private float precio_iva;
    private float costo_iva;
    private String fecha_compra;
    private MetodoPago meotod_pago;

    public Facturas() {
    }

    public Facturas(int id, int folio, float precio_neto, float precio_iva, float costo_iva, String fecha_compra, MetodoPago meotod_pago) {
        this.id = id;
        this.folio = folio;
        this.precio_neto = precio_neto;
        this.precio_iva = precio_iva;
        this.costo_iva = costo_iva;
        this.fecha_compra = fecha_compra;
        this.meotod_pago = meotod_pago;
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

    public float getPrecio_iva() {
        return precio_iva;
    }

    public void setPrecio_iva(float precio_iva) {
        this.precio_iva = precio_iva;
    }

    public float getCosto_iva() {
        return costo_iva;
    }

    public void setCosto_iva(float costo_iva) {
        this.costo_iva = costo_iva;
    }

    public String getFecha_compra() {
        return fecha_compra;
    }

    public void setFecha_compra(String fecha_compra) {
        this.fecha_compra = fecha_compra;
    }

    public MetodoPago getMeotod_pago() {
        return meotod_pago;
    }

    public void setMeotod_pago(MetodoPago meotod_pago) {
        this.meotod_pago = meotod_pago;
    }

    @Override
    public String toString() {
        return this.folio + " " + this.fecha_compra;
    }
    
    
}

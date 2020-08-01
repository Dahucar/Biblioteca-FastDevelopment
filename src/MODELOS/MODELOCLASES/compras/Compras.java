/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.MODELOCLASES.compras;

import MODELOS.MODELOCLASES.personas.Distribuidor;

/**
 *
 * @author Daniel Huenul
 */
public class Compras {
    private int id;
    private String codigo_compra;
    private Facturas factura;
    private Distribuidor distribuidor;

    public Compras() {
    }

    public Compras(int id, String codigo_compra, Facturas factura, Distribuidor distribuidor) {
        this.id = id;
        this.codigo_compra = codigo_compra;
        this.factura = factura;
        this.distribuidor = distribuidor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo_compra() {
        return codigo_compra;
    }

    public void setCodigo_compra(String codigo_compra) {
        this.codigo_compra = codigo_compra;
    }

    public Facturas getFactura() {
        return factura;
    }

    public void setFactura(Facturas factura) {
        this.factura = factura;
    }

    public Distribuidor getDistribuidor() {
        return distribuidor;
    }

    public void setDistribuidor(Distribuidor distribuidor) {
        this.distribuidor = distribuidor;
    }

    @Override
    public String toString() {
        return "Compras{" + "id=" + id + ", codigo_compra=" + codigo_compra + ", factura=[" + factura.toString() + "],"
                + " distribuidor=[" + distribuidor.mostrarDatos() + ']';
    }
    
    
}

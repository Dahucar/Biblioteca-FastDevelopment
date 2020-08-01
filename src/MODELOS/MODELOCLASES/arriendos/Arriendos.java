/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.MODELOCLASES.arriendos;

import MODELOS.MODELOCLASES.personas.Cliente;
import MODELOS.MODELOCLASES.personas.Trabajador;
import java.util.Date;

/**
 *
 * @author Daniel Huenul
 */
public class Arriendos {
    private int id;
    private String codigo;
    private float costo_total;
    private float costo_arriendo;
    private Date fecha_arriendo;
    private Date fecha_devolucion;
    private Date fecha_entrega;
    private int dias_atrazo;
    private float multa;
    private Trabajador trabjador;
    private Cliente cliente;

    public Arriendos() {
    }

    public Arriendos(int id, String codigo, float costo_total, float costo_arriendo, Date fecha_arriendo, Date fecha_devolucion, Date fecha_entrega, int dias_atrazo, float multa, Trabajador trabjador, Cliente cliente) {
        this.id = id;
        this.codigo = codigo;
        this.costo_total = costo_total;
        this.costo_arriendo = costo_arriendo;
        this.fecha_arriendo = fecha_arriendo;
        this.fecha_devolucion = fecha_devolucion;
        this.fecha_entrega = fecha_entrega;
        this.dias_atrazo = dias_atrazo;
        this.multa = multa;
        this.trabjador = trabjador;
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public float getCosto_total() {
        return costo_total;
    }

    public void setCosto_total(float costo_total) {
        this.costo_total = costo_total;
    }

    public float getCosto_arriendo() {
        return costo_arriendo;
    }

    public void setCosto_arriendo(float costo_arriendo) {
        this.costo_arriendo = costo_arriendo;
    }

    public Date getFecha_arriendo() {
        return fecha_arriendo;
    }

    public void setFecha_arriendo(Date fecha_arriendo) {
        this.fecha_arriendo = fecha_arriendo;
    }

    public Date getFecha_devolucion() {
        return fecha_devolucion;
    }

    public void setFecha_devolucion(Date fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }

    public Date getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(Date fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public int getDias_atrazo() {
        return dias_atrazo;
    }

    public void setDias_atrazo(int dias_atrazo) {
        this.dias_atrazo = dias_atrazo;
    }

    public float getMulta() {
        return multa;
    }

    public void setMulta(float multa) {
        this.multa = multa;
    }

    public Trabajador getTrabjador() {
        return trabjador;
    }

    public void setTrabjador(Trabajador trabjador) {
        this.trabjador = trabjador;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

      

    @Override
    public String toString() {
        return "Arriendos{" + "id=" + id + ", costo_total=" + costo_total + ", costo_arriendo=" + costo_arriendo + ", fecha_arriendo=" + fecha_arriendo + ", fecha_devolucion=" + fecha_devolucion + ", fecha_entrega=" + fecha_entrega + ", dias_atrazo=" + dias_atrazo + ", multa=" + multa + ", trabjador=" + trabjador + ", cliente=" + cliente + '}';
    } 
 
    public String mostrarDatos() {
        return "Arriendos{" + "id=" + id + ", costo_total=" + costo_total + ", costo_arriendo=" + costo_arriendo + ", fecha_arriendo=" + fecha_arriendo + ", fecha_devolucion=" + fecha_devolucion + ", fecha_entrega=" + fecha_entrega + ", dias_atrazo=" + dias_atrazo + ", multa=" + multa + ", [trabjador id=" + trabjador.getId() +"trabjador nombre="+trabjador.getNombre() +"], [cliente id=" + cliente.getId() +", cliente nombre rut" +cliente.getRut()+ ",";
    }
    
    
}

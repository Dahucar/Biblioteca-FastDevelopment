/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.ventas;

import MODELOS.Helpers;
import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER;
import MODELOS.MODELOCLASES.libros.Libro;
import MODELOS.MODELOCLASES.personas.Cliente;
import MODELOS.MODELOCLASES.personas.Trabajador;
import MODELOS.MODELOCLASES.ventas.Boletas;
import MODELOS.MODELOCLASES.ventas.Ventas;
import MODELOS.SQL.ventas.AsociarLibrosVentasSql;
import MODELOS.SQL.ventas.VentasSql;
import VISTAS.ventas.GestionarAsocLibVentas;
import VISTAS.ventas.GestionarVentas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Daniel Huenul
 */
public class ControladorVentas implements ActionListener {

    private Ventas modelo;
    private VentasSql sql;
    private GestionarVentas vista;

    public ControladorVentas(Ventas modelo, VentasSql sql, GestionarVentas vista) {
        this.modelo = modelo;
        this.sql = sql;
        this.vista = vista;
        this.vista.Submenu_salir.addActionListener(this);
        this.vista.Submenu_volver.addActionListener(this);
        this.vista.btn_add.addActionListener(this);
        this.vista.btn_remove.addActionListener(this);
        this.vista.btn_update.addActionListener(this);
        this.vista.btn_clear.addActionListener(this);
        this.vista.btn_generarCodigo.addActionListener(this);
        this.vista.btn_asociarLibros.addActionListener(this);
        muestrate();
    }

    public void muestrate() {
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de ventas");
        this.vista.campo_id.setVisible(false);
        this.vista.campo_codigoVenta.setEditable(false);

        llenar(this.vista.tabla);
        seleccionarTabla(this.vista.tabla);
        
        Helpers.cargarComboClientes(this.vista.combo_clientes);
        Helpers.cargarComboTrabajadores(this.vista.combo_trabajador);
        Helpers.cargarComboBoletas(this.vista.combo_boletas);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(this.vista.btn_add)) {
            add();
        }

        if (ae.getSource().equals(this.vista.btn_remove)) {
            remove();
        }

        if (ae.getSource().equals(this.vista.btn_update)) {
            update();
        }

        if (ae.getSource().equals(this.vista.btn_clear)) {
            limpiar();
        }

        if (ae.getSource().equals(this.vista.btn_asociarLibros)) {
            asociarLibros();
        }
 
        if (ae.getSource().equals(this.vista.btn_generarCodigo)) {
            this.vista.campo_codigoVenta.setText(String.valueOf(Helpers.numAleatorio()));
        }
        
        if (ae.getSource().equals(this.vista.Submenu_volver)) {
            VOLVER(this.vista);
        }

        if (ae.getSource().equals(this.vista.Submenu_salir)) {
            SALIR(this.vista);
        }
    }

    private boolean llenar(JTable tabla) {
        DefaultTableModel modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int filas, int columnas) {
                if (columnas == tabla.getColumnCount()) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        tabla.setModel(modeloTabla);
        tabla.getTableHeader().setReorderingAllowed(false);
        //idVenta	codigoVenta 
        //idCliente | rutCliente | nombre | apellido_p | apellido_m | fecha_contrato	
        //idTrabajador | rutTrabajador | nombre | apellido_p | apellido_m
        //idBoleta | folio | precio_neto | costo_iva | precio_iva | fecha_venta | 
        //MetodosPagos_idMetodoPago	idMetodoPago	metodo	
        modeloTabla.addColumn("#");
        modeloTabla.addColumn("Cod. Venta");
        modeloTabla.addColumn("Rut Cli.");
        modeloTabla.addColumn("Rut Trab.");
        modeloTabla.addColumn("Folio boleta");
        modeloTabla.addColumn("Metodo de pago");

        Object[] colum = new Object[6];
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getId();
            colum[1] = sql.listar().get(i).getCodigo_venta();
            colum[2] = sql.listar().get(i).getCliente().getRut();
            colum[3] = sql.listar().get(i).getTrabajador().getRut();
            colum[4] = sql.listar().get(i).getBoleta().getFolio();
            colum[5] = sql.listar().get(i).getBoleta().getMetodo_pago().getMetodo();

            modeloTabla.addRow(colum);
        }

        return true;
    }

    private void seleccionarTabla(JTable tb) {
        tb.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tb.getSelectedRow();
                int id = Integer.parseInt(tb.getValueAt(fila, 0).toString());
                modelo.setId(id);

                if (sql.SELECT(modelo)) {
                    vista.campo_codigoVenta.setText(String.valueOf(modelo.getCodigo_venta()));

                    ComboBoxModel modeloCombo = vista.combo_clientes.getModel();
                    for (int i = 0; i < modeloCombo.getSize(); i++) {
                        if (modeloCombo.getElementAt(i).toString().equals(modelo.getCliente().toString())) {
                            vista.combo_clientes.setSelectedIndex(i);
                            break;
                        }
                    }

                    ComboBoxModel modeloComboTrabajadores = vista.combo_trabajador.getModel();
                    for (int i = 0; i < modeloComboTrabajadores.getSize(); i++) {
                        System.out.println("TM -> "+modeloComboTrabajadores.getElementAt(i).toString());
                        System.out.println("MM -> "+modelo.getTrabajador().toString());
                        if (modeloComboTrabajadores.getElementAt(i).toString().equals(modelo.getTrabajador().toString())) {
                            System.err.println("SIIII   ");
                            vista.combo_trabajador.setSelectedIndex(i);
                            break;
                        }else{
                            System.err.println("NOOOOO");
                        }
                    }

                    ComboBoxModel modeloComboBoletas = vista.combo_boletas.getModel();
                    for (int i = 0; i < modeloComboBoletas.getSize(); i++) {
                        if (modeloComboBoletas.getElementAt(i).toString().equals(modelo.getBoleta().toString())) {
                            vista.combo_boletas.setSelectedIndex(i);
                            break;
                        }
                    }

                    System.out.println(modelo.toString());

                }
            }
        });
    }

    private void add() {
        try {
            if (validaCampos()) {

                this.modelo.setId(modelo.getId());
                this.modelo.setCodigo_venta(this.vista.campo_codigoVenta.getText());
                this.modelo.setTrabajador((Trabajador) this.vista.combo_trabajador.getSelectedItem());
                this.modelo.setCliente((Cliente) this.vista.combo_clientes.getSelectedItem()); 
                this.modelo.setBoleta((Boletas) this.vista.combo_boletas.getSelectedItem()); 
                if (sql.Verificar(modelo) == 0) {
                    if (sql.INSERT(modelo)) {
                        JOptionPane.showMessageDialog(null, "Venta registrada.");
                        llenar(this.vista.tabla);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ya se ha registrado la ventta");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Usted debe llenar los campos");
            }
        } catch (Exception e) {
            System.err.println("addestado: " + e);
        }
    }

    private void remove() {
        try {
            if (validaCampos()) {  
                int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este registro?");
                if (opSelect == JOptionPane.YES_OPTION) { 
                    if (sql.REMOVE(modelo)) {
                        JOptionPane.showMessageDialog(null, "Venta eliminado");
                        llenar(this.vista.tabla);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "error");
                    } 
                } 
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un elemento de la tabla");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }

    private void update() {
        try {
            if (validaCampos()) {
                this.modelo.setId(modelo.getId());
                this.modelo.setCodigo_venta(this.vista.campo_codigoVenta.getText());
                this.modelo.setTrabajador((Trabajador) this.vista.combo_trabajador.getSelectedItem());
                this.modelo.setCliente((Cliente) this.vista.combo_clientes.getSelectedItem()); 
                this.modelo.setBoleta((Boletas) this.vista.combo_boletas.getSelectedItem());  
                int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea actualizar este registro?");
                if (opSelect == JOptionPane.YES_OPTION) { 
                    if (sql.UPDATE(modelo)) {
                        JOptionPane.showMessageDialog(null, "Libro actualizado");
                        llenar(this.vista.tabla);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error");
                    } 
                }
            } 
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }

    private boolean validaCampos() {
        boolean est = true;
        if (this.vista.campo_codigoVenta.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.combo_clientes.getSelectedIndex() == 0) {
            est = false;
        }
        if (this.vista.combo_trabajador.getSelectedIndex() == 0) {
            est = false;
        }
        if (this.vista.combo_boletas.getSelectedIndex() == 0) {
            est = false;
        }
        return est;
    }

    private void limpiar() {
        this.vista.campo_codigoVenta.setText(null);
        this.vista.combo_clientes.setSelectedIndex(0);
        this.vista.combo_trabajador.setSelectedIndex(0);
        this.vista.combo_boletas.setSelectedIndex(0);
    }

    private void asociarLibros() {
//        try {
            
            if(validaCampos()){ 
                //el modelo ya esta lleno 
                Libro lib = new Libro();
                GestionarAsocLibVentas asoc = new GestionarAsocLibVentas();
                AsociarLibrosVentasSql ausql = new AsociarLibrosVentasSql();
                ControladorAsociarLibVenta contrl = new ControladorAsociarLibVenta(lib, modelo, ausql, asoc);
                this.vista.dispose();
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione un libro de la tabla");
            }
            
//        } catch (Exception e) {
//            System.err.println("asociarAutor: " + e);
//        }
    }

}

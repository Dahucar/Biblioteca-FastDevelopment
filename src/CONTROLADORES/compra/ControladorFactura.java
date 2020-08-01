/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.compra;

import MODELOS.Helpers;
import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER;
import MODELOS.MODELOCLASES.compras.Facturas;
import MODELOS.MODELOCLASES.ventas.MetodoPago;
import MODELOS.SQL.compras.FacturasSql;
import VISTAS.compras.GestionarFacturas;
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
public class ControladorFactura implements ActionListener {

    private Facturas modelo;
    private FacturasSql sql;
    private GestionarFacturas vista;

    public ControladorFactura(Facturas modelo, FacturasSql sql, GestionarFacturas vista) {
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

        muestrate();
    }

    public void muestrate() {
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de facturas");
        this.vista.campo_folio.setEditable(false);
        llenar(this.vista.Tabla);
        seleccionarTabla(this.vista.Tabla);
        Helpers.cargarComboMetodo(this.vista.combo_metodosPago);
//        buscarEnTabla(this.vista.campo_buscar, this.vista.Tabla_estados);
        Helpers.soloNumeros(this.vista.campo_costoIva);
        Helpers.soloNumeros(this.vista.campo_precioIva);
        Helpers.soloNumeros(this.vista.campo_precioNeto);
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

        if (ae.getSource().equals(this.vista.btn_generarCodigo)) {
            this.vista.campo_folio.setText(String.valueOf(Helpers.numAleatorio()));
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

        modeloTabla.addColumn("#");
        modeloTabla.addColumn("Precio neto");
        modeloTabla.addColumn("Precio IVA");
        modeloTabla.addColumn("Costo IVA");
        modeloTabla.addColumn("Fecha de compra");
        modeloTabla.addColumn("Metodo de pago");

        Object[] colum = new Object[6];
        for (int i = 0; i < sql.listar().size(); i++) {
            //idFactura	folio	precio_neto	
            //precio_iva	costo_iva	fecha_compra	
            //MetodosPagos_idMetodoPago	idMetodoPago	metodo
            colum[0] = sql.listar().get(i).getId();
            colum[1] = sql.listar().get(i).getPrecio_neto();
            colum[2] = sql.listar().get(i).getPrecio_iva();
            colum[3] = sql.listar().get(i).getCosto_iva();
            colum[4] = sql.listar().get(i).getFecha_compra();
            colum[5] = sql.listar().get(i).getMeotod_pago().getMetodo();

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
                    vista.campo_folio.setText(String.valueOf(modelo.getFolio()));
                    vista.campo_precioNeto.setText(String.valueOf(modelo.getPrecio_neto()));
                    vista.campo_precioIva.setText(String.valueOf(modelo.getPrecio_iva()));
                    vista.campo_costoIva.setText(String.valueOf(modelo.getCosto_iva()));

                    ComboBoxModel modeloCombo = vista.combo_metodosPago.getModel();
                    for (int i = 0; i < modeloCombo.getSize(); i++) {
                        if (modeloCombo.getElementAt(i).toString().equals(modelo.getMeotod_pago().getMetodo())) {
                            vista.combo_metodosPago.setSelectedIndex(i);
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
                this.modelo.setFolio(Integer.parseInt(this.vista.campo_folio.getText()));
                this.modelo.setCosto_iva(Float.parseFloat(this.vista.campo_costoIva.getText()));
                this.modelo.setPrecio_iva(Float.parseFloat(this.vista.campo_precioIva.getText()));
                this.modelo.setPrecio_neto(Float.parseFloat(this.vista.campo_precioNeto.getText()));
                this.modelo.setMeotod_pago((MetodoPago) this.vista.combo_metodosPago.getSelectedItem());

                System.out.println(this.modelo.toString());

                if (sql.VerificarFolio(modelo) == 0) {
                    if (sql.INSERT(modelo)) {
                        JOptionPane.showMessageDialog(null, "Factura registrada.");
                        llenar(this.vista.Tabla);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ya se ha registrado al Factura");
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
                        JOptionPane.showMessageDialog(null, "Libro eliminado");
                        llenar(this.vista.Tabla);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "error");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un registro de la tabla");
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
                this.modelo.setFolio(Integer.parseInt(this.vista.campo_folio.getText()));
                this.modelo.setCosto_iva(Float.parseFloat(this.vista.campo_costoIva.getText()));
                this.modelo.setPrecio_iva(Float.parseFloat(this.vista.campo_precioIva.getText()));
                this.modelo.setPrecio_neto(Float.parseFloat(this.vista.campo_precioNeto.getText()));
                this.modelo.setMeotod_pago((MetodoPago) this.vista.combo_metodosPago.getSelectedItem());

                int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea actualizar este registro?");
                if (opSelect == JOptionPane.YES_OPTION) {

                    if (sql.UPDATE(modelo)) {
                        JOptionPane.showMessageDialog(null, "Factura actualizado");
                        llenar(this.vista.Tabla);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error");
                    }

                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un registro de la tabla");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }

    private boolean validaCampos() {
        boolean est = true;
        if (this.vista.campo_costoIva.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.campo_folio.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.campo_precioIva.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.campo_precioNeto.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.combo_metodosPago.getSelectedIndex() == 0) {
            est = false;
        }
        return est;
    }

    private void limpiar() {
        this.vista.campo_costoIva.setText(null);
        this.vista.campo_folio.setText(null);
        this.vista.campo_precioIva.setText(null);
        this.vista.campo_precioNeto.setText(null);
        this.vista.combo_metodosPago.setSelectedIndex(0);
    }

}

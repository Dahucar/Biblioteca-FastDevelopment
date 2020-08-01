/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.ventas;

import MODELOS.Helpers;
import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER;
import MODELOS.MODELOCLASES.ventas.Boletas;
import MODELOS.MODELOCLASES.ventas.MetodoPago;
import MODELOS.SQL.ventas.BoletaSql;
import VISTAS.ventas.GestionarBoletas;
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
public class ControladorBoleta implements ActionListener {

    private Boletas modelo;
    private BoletaSql sql;
    private GestionarBoletas vista;

    public ControladorBoleta(Boletas modelo, BoletaSql sql, GestionarBoletas vista) {
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
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de boletas");
        this.vista.campo_id.setVisible(false);
        this.vista.campo_folio.setEditable(false);

        llenar(this.vista.Tabla_estados);
        seleccionarTabla(this.vista.Tabla_estados);

        Helpers.soloNumeros(this.vista.campo_costoIva);
        Helpers.soloNumeros(this.vista.campo_precioIva);
        Helpers.soloNumeros(this.vista.campo_precioNeto);

        Helpers.cargarComboMetodo(this.vista.combo_metodoPago);
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
        //idBoleta	folio	precio_neto	costo_iva	
        //precio_iva	fecha_venta	MetodosPagos_idMetodoPago	
        //idMetodoPago	metodo	
        modeloTabla.addColumn("#");
        modeloTabla.addColumn("Folio");
        modeloTabla.addColumn("Precio Neto");
        modeloTabla.addColumn("Costo IVA");
        modeloTabla.addColumn("Precio IVA");
        modeloTabla.addColumn("Fecha de venta");
        modeloTabla.addColumn("Metodo de pago");

        Object[] colum = new Object[7];
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getId();
            colum[1] = sql.listar().get(i).getFolio();
            colum[2] = sql.listar().get(i).getPrecio_neto();
            colum[3] = sql.listar().get(i).getCosto_iva();
            colum[4] = sql.listar().get(i).getPrecio_iva();
            colum[5] = sql.listar().get(i).getFecha_venta();
            colum[6] = sql.listar().get(i).getMetodo_pago().getMetodo();

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
                    vista.campo_costoIva.setText(String.valueOf(modelo.getCosto_iva()));
                    vista.campo_precioIva.setText(String.valueOf(modelo.getPrecio_iva()));

                    ComboBoxModel modeloCombo = vista.combo_metodoPago.getModel();
                    for (int i = 0; i < modeloCombo.getSize(); i++) {
                        if (modeloCombo.getElementAt(i).toString().equals(modelo.getMetodo_pago().getMetodo())) {
                            vista.combo_metodoPago.setSelectedIndex(i);
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
                this.modelo.setPrecio_neto(Float.parseFloat(this.vista.campo_precioNeto.getText()));
                this.modelo.setCosto_iva(Float.parseFloat(this.vista.campo_costoIva.getText()));
                this.modelo.setPrecio_iva(Float.parseFloat(this.vista.campo_precioIva.getText()));
                this.modelo.setMetodo_pago((MetodoPago) this.vista.combo_metodoPago.getSelectedItem());

                System.out.println(this.modelo.toString());

                if (sql.Verificar(modelo) == 0) {
                    if (sql.INSERT(modelo)) {
                        JOptionPane.showMessageDialog(null, "Boleta registrada.");
                        llenar(this.vista.Tabla_estados);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ya se ha registrado al libro");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Usted debe llenar los campos");
            }
        } catch (Exception e) {
            System.err.println("addestado: " + e);
        }
    }

    private void update() {
        try {
            if (validaCampos()) {
                modelo.setId(modelo.getId());
                modelo.setFolio(Integer.parseInt(vista.campo_folio.getText()));
                modelo.setPrecio_neto(Float.parseFloat(vista.campo_precioNeto.getText()));
                modelo.setCosto_iva(Float.parseFloat(vista.campo_costoIva.getText()));
                modelo.setPrecio_iva(Float.parseFloat(vista.campo_precioIva.getText()));
                modelo.setMetodo_pago((MetodoPago) vista.combo_metodoPago.getSelectedItem());

                int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea actualizar este registro?");
                if (opSelect == JOptionPane.YES_OPTION) {

                    if (sql.UPDATE(modelo)) {
                        JOptionPane.showMessageDialog(null, "Boleta actualizada");
                        llenar(this.vista.Tabla_estados);
                        limpiar();
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

    private void remove() {
        try {
            if (validaCampos()) {
                int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este registro?");
                if (opSelect == JOptionPane.YES_OPTION) {
                    if (sql.REMOVE(modelo)) {
                        JOptionPane.showMessageDialog(null, "Boleta eliminada");
                        llenar(this.vista.Tabla_estados);
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
        if (this.vista.combo_metodoPago.getSelectedIndex() == 0) {
            est = false;
        }
        return est;
    }

    private void limpiar() {
        this.vista.campo_costoIva.setText(null);
        this.vista.campo_folio.setText(null);
        this.vista.campo_precioIva.setText(null);
        this.vista.campo_precioNeto.setText(null);
        this.vista.combo_metodoPago.setSelectedIndex(0);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.ventas;

import MODELOS.Helpers;
import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER;
import MODELOS.MODELOCLASES.ventas.MetodoPago;
import MODELOS.SQL.ventas.MetodoPagoSql;
import VISTAS.ventas.GestionarMedioPago;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Daniel Huenul
 */
public class ControladorMedioPago implements ActionListener {

    private MetodoPago modelo;
    private MetodoPagoSql sql;
    private GestionarMedioPago vista;

    public ControladorMedioPago(MetodoPago modelo, MetodoPagoSql sql, GestionarMedioPago vista) {
        this.modelo = modelo;
        this.sql = sql;
        this.vista = vista;
        this.vista.Submenu_salir.addActionListener(this);
        this.vista.Submenu_volver.addActionListener(this);
        this.vista.btn_add.addActionListener(this);
        this.vista.btn_remove.addActionListener(this);
        this.vista.btn_update.addActionListener(this);
        muestrate();
    }

    public void muestrate() {
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de medios de pago");
        this.vista.campo_id.setVisible(false);

        llenar(this.vista.tabla);
        seleccionarTabla(this.vista.tabla);
        Helpers.soloLetras(this.vista.campo_mediopago);
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
        modeloTabla.addColumn("Medio de pago");

        Object[] colum = new Object[2];
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getId();
            colum[1] = sql.listar().get(i).getMetodo();
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

                if (sql.Verificar(modelo)) {
                    vista.campo_mediopago.setText(String.valueOf(modelo.getMetodo()));
                }
            }
        });
    }

    private void add() {
        try {
            if (!this.vista.campo_mediopago.getText().isEmpty()) { 
                this.modelo.setMetodo(this.vista.campo_mediopago.getText());

                if (!sql.VerificarMetodo(modelo).equals(this.vista.campo_mediopago.getText())) {
                    if (sql.INSERT(modelo)) {
                        JOptionPane.showMessageDialog(null, "Metodo registrado.");
                        llenar(this.vista.tabla);
                        this.vista.campo_mediopago.setText(null);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ya se ha registrado el Metodo");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Usted debe ingresar el nombre del estado");
            }

        } catch (Exception e) {
            System.err.println("addestado: " + e);
        }
    }

    private void remove() {
        try {
            if (!vista.campo_mediopago.getText().isEmpty()) {
                modelo.setMetodo(vista.campo_mediopago.getText());
                if (sql.VerificarMetodo(modelo).equals(modelo.getMetodo())) {

                    int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este registro?");
                    if (opSelect == JOptionPane.YES_OPTION) {

                        if (sql.REMOVE(modelo)) {
                            JOptionPane.showMessageDialog(null, "Estado eliminado");
                            llenar(this.vista.tabla);
                            this.vista.campo_mediopago.setText(null);
                        } else {
                            JOptionPane.showMessageDialog(null, "error");
                        }

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "El metodo no existe");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar el metodo");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }

    private void update() {
        try {
            if (!vista.campo_mediopago.getText().isEmpty()) {
                modelo.setMetodo(vista.campo_mediopago.getText());

                String resp = JOptionPane.showInputDialog("Ingrese nuevo nombre");

                if (!sql.VerificarMetodo(modelo).equals(resp)) {

                    if (resp != null && !"".equals(resp)) {
                        if (sql.UPDATE(resp, modelo.getId())) {
                            JOptionPane.showMessageDialog(null, "Actualizado");
                            llenar(this.vista.tabla);
                            this.vista.campo_mediopago.setText(null);

                        } else {
                            JOptionPane.showMessageDialog(null, "Error");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Recuerde... Para actualizar ingrese contenido");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "El estado ingresdo ya esta registrado");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }
}

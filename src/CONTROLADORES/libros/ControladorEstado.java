/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.libros;

import MODELOS.Helpers;
import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER; 
import MODELOS.MODELOCLASES.libros.Estado;
import MODELOS.SQL.libro.EstadoSql;
import VISTAS.libros.GestionarEstado;
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
public class ControladorEstado implements ActionListener {

    private Estado modelo;
    private EstadoSql sql;
    private GestionarEstado vista;

    public ControladorEstado(Estado modelo, EstadoSql sql, GestionarEstado vista) {
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
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de estados");

        llenar(this.vista.Tabla_estados);
        seleccionarTabla(this.vista.Tabla_estados);
 
        Helpers.soloLetras(this.vista.campo_cateogira);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(this.vista.btn_add)) {
            addEstado();
        }

        if (ae.getSource().equals(this.vista.btn_remove)) {
            removeEstado();
        }

        if (ae.getSource().equals(this.vista.btn_update)) {
            updateEstado();
        }

        if (ae.getSource().equals(this.vista.Submenu_volver)) {
            VOLVER(this.vista);
        }

        if (ae.getSource().equals(this.vista.Submenu_salir)) {
            SALIR(this.vista);
        }
    }

    private void addEstado() {
        try {
            if (!this.vista.campo_cateogira.getText().isEmpty()) {
                this.modelo.setId(0);
                this.modelo.setEstado(this.vista.campo_cateogira.getText());

                if (!sql.VerificarEstado(modelo).equals(this.vista.campo_cateogira.getText())) {
                    if (sql.INSERT(modelo)) {
                        JOptionPane.showMessageDialog(null, "Estado registrado.");
                        llenar(this.vista.Tabla_estados);
                        this.vista.campo_cateogira.setText(null);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ya se ha registrado el estado ingresado");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Usted debe ingresar el nombre del estado");
            }

        } catch (Exception e) {
            System.err.println("addestado: " + e);
        }
    }

    private void updateEstado() {
        try {
            if (!vista.campo_cateogira.getText().isEmpty()) {
                modelo.setEstado(vista.campo_cateogira.getText());
                if (sql.getIdEstado(modelo) != -1) {

                    int idObtenido = sql.getIdEstado(modelo);

                    String resp = JOptionPane.showInputDialog("Ingrese nuevo nombre");

                    if (!sql.VerificarEstado(modelo).equals(resp)) {

                        if (resp != null && !"".equals(resp)) {
                            if (sql.UPDATE(resp, idObtenido)) {
                                JOptionPane.showMessageDialog(null, "Actualizado");
                                llenar(this.vista.Tabla_estados);
                                this.vista.campo_cateogira.setText(null);

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
                    JOptionPane.showMessageDialog(null, "No existe el estado ingresado");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar el estado");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }

    private void removeEstado() {
        try {
            if (!vista.campo_cateogira.getText().isEmpty()) {
                modelo.setEstado(vista.campo_cateogira.getText());
                if (sql.VerificarEstado(modelo).equals(modelo.getEstado())) {

                    int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este registro?");
                    if (opSelect == JOptionPane.YES_OPTION) {

                        if (sql.REMOVE(modelo)) {
                            JOptionPane.showMessageDialog(null, "Estado eliminado");
                            llenar(this.vista.Tabla_estados);
                            this.vista.campo_cateogira.setText(null);
                        } else {
                            JOptionPane.showMessageDialog(null, "error");
                        }

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "El estado no existe");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar el estado");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }

    private boolean llenar(JTable tabla) {
        DefaultTableModel modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int filas, int columnas) {
                if (columnas == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        tabla.setModel(modeloTabla);
        tabla.getTableHeader().setReorderingAllowed(false);

        modeloTabla.addColumn("Estado");

        Object[] colum = new Object[1];
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getEstado();

            modeloTabla.addRow(colum);
        }

        return true;
    }

    private void seleccionarTabla(JTable tb) {
        tb.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tb.getSelectedRow();
                String estado = tb.getValueAt(fila, 0).toString();
                modelo.setEstado(estado);

                if (sql.VerificarEstado(modelo).equals(estado)) {
                    vista.campo_cateogira.setText(String.valueOf(modelo.getEstado()));
                }
            }
        });
    }

}

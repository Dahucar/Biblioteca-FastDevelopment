/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.libros;

import MODELOS.Helpers;
import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER; 
import MODELOS.MODELOCLASES.libros.Editorial;
import MODELOS.SQL.libro.EditorialSql;
import VISTAS.libros.GestionarEditorial;
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
public class ControladorEditorial implements ActionListener {

    private Editorial modelo;
    private EditorialSql sql;
    private GestionarEditorial vista;

    public ControladorEditorial(Editorial modelo, EditorialSql sql, GestionarEditorial vista) {
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
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de editoriales");

        llenar(this.vista.Tabla);
        seleccionarTabla(this.vista.Tabla);
 
//        Helpers.soloLetras(this.vista.campo_nombre);
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
                if (columnas == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        tabla.setModel(modeloTabla);
        tabla.getTableHeader().setReorderingAllowed(false);

        modeloTabla.addColumn("Nombre editorial");

        Object[] colum = new Object[1];
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getNombre();

            modeloTabla.addRow(colum);
        }

        return true;
    }

    private void seleccionarTabla(JTable tb) {
        tb.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tb.getSelectedRow();
                String nombre = tb.getValueAt(fila, 0).toString();
                modelo.setNombre(nombre);

                if (sql.Verificar(modelo).equals(nombre)) {
                    vista.campo_nombre.setText(String.valueOf(modelo.getNombre()));
                }
            }
        });
    }

    private void add() {
        try {
            if (!this.vista.campo_nombre.getText().isEmpty()) {
                this.modelo.setId(0);
                this.modelo.setNombre(this.vista.campo_nombre.getText());

                if (!sql.Verificar(modelo).equals(this.vista.campo_nombre.getText())) {
                    if (sql.INSERT(modelo)) {
                        JOptionPane.showMessageDialog(null, "Editorial registrado.");
                        llenar(this.vista.Tabla);
                        this.vista.campo_nombre.setText(null);
                        this.vista.campo_nombre.setText(null);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ya se ha registrado la editorial");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Usted debe ingresar el nombre de la editorial");
            }

        } catch (Exception e) {
            System.err.println("addestado: " + e);
        }
    }

    private void remove() {
        try {
            if (!vista.campo_nombre.getText().isEmpty()) {
                modelo.setNombre(vista.campo_nombre.getText());
                if (sql.Verificar(modelo).equals(modelo.getNombre())) {

                    int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este registro?");
                    if (opSelect == JOptionPane.YES_OPTION) {

                        if (sql.REMOVE(modelo)) {
                            JOptionPane.showMessageDialog(null, "Editorial eliminada");
                            llenar(this.vista.Tabla);
                            this.vista.campo_nombre.setText(null);
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

    private void update() {
        try {
            if (!vista.campo_nombre.getText().isEmpty()) {
                modelo.setNombre(vista.campo_nombre.getText());
                if (sql.getIdEditorial(modelo) != -1) {

                    int idObtenido = sql.getIdEditorial(modelo);
                    String resp = JOptionPane.showInputDialog("Ingrese nuevo nombre");

                    if (!sql.Verificar(modelo).equals(resp)) {

                        if (resp != null && !"".equals(resp)) {
                            if (sql.UPDATE(resp, idObtenido)) {
                                JOptionPane.showMessageDialog(null, "Actualizado");
                                llenar(this.vista.Tabla);
                                this.vista.campo_nombre.setText(null);

                            } else {
                                JOptionPane.showMessageDialog(null, "Error");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Recuerde... Para actualizar ingrese contenido");
                        }

                    }else{
                            JOptionPane.showMessageDialog(null, "La editorial ingresda ya esta registrada");
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

}

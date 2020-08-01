/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.personas.datos;

import MODELOS.Helpers;
import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER;
import MODELOS.MODELOCLASES.personas.datos.Correo;
import MODELOS.SQL.personas.datos.CorreoSql;
import VISTAS.personas.datos.GestionarCorreos;
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
public class ControladorCorreo implements ActionListener {

    private Correo modelo;
    private CorreoSql sql;
    private GestionarCorreos vista;

    public ControladorCorreo(Correo modelo, CorreoSql sql, GestionarCorreos vista) {
        this.modelo = modelo;
        this.sql = sql;
        this.vista = vista;
        this.vista.Submenu_salir.addActionListener(this);
        this.vista.Submenu_volver.addActionListener(this);

        muestrate();
    }

    public void muestrate() {
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de correos");
        this.vista.campo_id.setVisible(false);
        this.vista.btn_add.addActionListener(this);
        this.vista.btn_remove.addActionListener(this);
        this.vista.btn_update.addActionListener(this);

        llenar(this.vista.Tabla_estados);
        seleccionarTabla(this.vista.Tabla_estados);
//
//        buscarEnTabla(this.vista.campo_buscar, this.vista.Tabla_estados);
//        Helpers.soloLetras(this.vista.campo_cateogira);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(this.vista.Submenu_volver)) {
            VOLVER(this.vista);
        }

        if (ae.getSource().equals(this.vista.Submenu_salir)) {
            SALIR(this.vista);
        }

        if (ae.getSource().equals(this.vista.btn_add)) {
            add();
        }

        if (ae.getSource().equals(this.vista.btn_remove)) {
            remove();
        }

        if (ae.getSource().equals(this.vista.btn_update)) {
            update();
        }
    }

    private void add() {
        try {
            if (validaCampos()) {
                if (Helpers.validarMail(this.vista.campo_correo.getText())) {

                    this.modelo.setId(0);
                    this.modelo.setCorreo(this.vista.campo_correo.getText());
                    this.modelo.setContrasenna(this.vista.campo_contraseña.getText());

                    if (!sql.VerificarCorreo(modelo).equals(modelo.getCorreo())) {
                        if (sql.INSERT(modelo)) {
                            JOptionPane.showMessageDialog(null, "Correo registrado.");
                            llenar(this.vista.Tabla_estados);
                            limpiar();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Ya se ha registrado el correo");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Formato de email incorrecto");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usted debe llenar los campos");
            }

        } catch (Exception e) {
            System.err.println("addestado: " + e);
        }
    }

    public boolean validaCampos() {
        boolean est = true;

        if (this.vista.campo_contraseña.getText().isEmpty()) {
            est = false;
        }

        if (this.vista.campo_correo.getText().isEmpty()) {
            est = false;
        }

        return est;
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
        modeloTabla.addColumn("Correo");
        Object[] colum = new Object[1];
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getCorreo();
            modeloTabla.addRow(colum);
        }

        return true;
    }

    private void remove() {
        try {
            if (!vista.campo_correo.getText().isEmpty()) {
                String mail = vista.campo_correo.getText();
                modelo.setCorreo(vista.campo_correo.getText());

                if (sql.VerificarCorreo(modelo).equals(mail)) {

                    int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este registro?");
                    if (opSelect == JOptionPane.YES_OPTION) {

                        if (sql.REMOVE(modelo)) {
                            JOptionPane.showMessageDialog(null, "Correo eliminada");
                            llenar(this.vista.Tabla_estados);
                            limpiar();
                        } else {
                            JOptionPane.showMessageDialog(null, "error");
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "El correo no existe");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usted debe llenar los campos");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }

    private void update() {
        try {
            if (validaCampos()) {
                modelo.setCorreo(vista.campo_correo.getText());
                modelo.setContrasenna(vista.campo_contraseña.getText());
                 
 
                    int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea actualizar este registro?");
                    if (opSelect == JOptionPane.YES_OPTION) {

                        if (sql.UPDATE(modelo)) {
                            JOptionPane.showMessageDialog(null, "Correo actualizado");
                            llenar(this.vista.Tabla_estados);
                        } else {
                            JOptionPane.showMessageDialog(null, "Error");
                        }

                    }

                 
            } else {
                JOptionPane.showMessageDialog(null, "Usted debe llenar los campos");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }

    private void limpiar() {
        vista.campo_contraseña.setText(null);
        vista.campo_correo.setText(null);
    }

    private void seleccionarTabla(JTable tb) {
        tb.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tb.getSelectedRow();
                String mail = tb.getValueAt(fila, 0).toString();
                modelo.setCorreo(mail);

                if (sql.VerificarCorreo(modelo).equals(mail)) {
                    vista.campo_correo.setText(String.valueOf(modelo.getCorreo()));
                    vista.campo_contraseña.setText(String.valueOf(modelo.getContrasenna()));
                    System.out.println(modelo.toString());
                }
            }
        });
    }
}

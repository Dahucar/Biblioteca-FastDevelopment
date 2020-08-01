/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.personas.datos;

import MODELOS.Helpers;
import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER;
import MODELOS.MODELOCLASES.personas.datos.Telefono;
import MODELOS.SQL.personas.datos.TelefonoSql;
import VISTAS.personas.datos.GestionarTelefonos;
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
public class ControladorTelefono implements ActionListener{
    
    private Telefono modelo;
    private TelefonoSql sql;
    private GestionarTelefonos vista;

    public ControladorTelefono(Telefono modelo, TelefonoSql sql, GestionarTelefonos vista) {
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
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de telefonos");
        this.vista.campo_idnumero.setVisible(false);

        llenar(this.vista.Tabla_estados);
        seleccionarTabla(this.vista.Tabla_estados);
// 
        Helpers.soloNumeros(this.vista.campo_numero);
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
        modeloTabla.addColumn("Numero"); 
        Object[] colum = new Object[1];
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getNumero(); 
            modeloTabla.addRow(colum);
        }

        return true;
    }
 
    private void add() {
        try {
            if (!this.vista.campo_numero.getText().isEmpty()) {
                this.modelo.setId(0);
                this.modelo.setNumero(this.vista.campo_numero.getText());

                if (!sql.VerificarNum(modelo).equals(this.vista.campo_numero.getText())) {
                    if (sql.INSERT(modelo)) {
                        JOptionPane.showMessageDialog(null, "Numero registrado.");
                        llenar(this.vista.Tabla_estados);
                        this.vista.campo_numero.setText(null);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ya se ha registrado el numero ingresado");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Usted debe ingresar el numero");
            }

        } catch (Exception e) {
            System.err.println("addestado: " + e);
        }
    }
    
    private void update() {
        try {
            if (!vista.campo_numero.getText().isEmpty()) {
                modelo.setNumero(vista.campo_numero.getText());
                if (sql.getIdNum(modelo) != -1) {

                    int idObtenido = sql.getIdNum(modelo);

                    String resp = JOptionPane.showInputDialog("Ingrese nuevo numero");

                    if (!sql.VerificarNum(modelo).equals(resp)) {

                        if (resp != null && !"".equals(resp)) {
                            if (sql.UPDATE(resp, idObtenido)) {
                                JOptionPane.showMessageDialog(null, "Actualizado");
                                llenar(this.vista.Tabla_estados);
                                this.vista.campo_numero.setText(null);

                            } else {
                                JOptionPane.showMessageDialog(null, "Error");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Recuerde... Para actualizar ingrese contenido");
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "El numero ingresdo ya esta registrado");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No existe el numero ingresado");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar el numero");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }
    
    private void remove() {
        try {
            if (!vista.campo_numero.getText().isEmpty()) {
                modelo.setNumero(vista.campo_numero.getText());
                if (sql.VerificarNum(modelo).equals(modelo.getNumero())) {

                    int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este registro?");
                    if (opSelect == JOptionPane.YES_OPTION) {

                        if (sql.REMOVE(modelo)) {
                            JOptionPane.showMessageDialog(null, "Numero eliminado");
                            llenar(this.vista.Tabla_estados);
                            this.vista.campo_numero.setText(null);
                        } else {
                            JOptionPane.showMessageDialog(null, "error");
                        }

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "El numero no existe");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar el numero");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }

    private void seleccionarTabla(JTable tb) {
        tb.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tb.getSelectedRow();
                String num = tb.getValueAt(fila, 0).toString();
                modelo.setNumero(num);

                if (sql.VerificarNum(modelo).equals(num)) {
                    vista.campo_numero.setText(String.valueOf(modelo.getNumero()));
                }
            }
        });
    }

}

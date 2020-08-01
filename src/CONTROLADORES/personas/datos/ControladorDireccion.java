/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.personas.datos;

import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER;
import MODELOS.MODELOCLASES.personas.datos.Direccion;
import MODELOS.SQL.personas.datos.DireccionSql;
import VISTAS.personas.datos.GestionarDirecciones;
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
public class ControladorDireccion implements ActionListener{
    
    private Direccion modelo;
    private DireccionSql sql;
    private GestionarDirecciones vista;

    public ControladorDireccion(Direccion modelo, DireccionSql sql, GestionarDirecciones vista) {
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
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de direcciones");
        this.vista.campo_iddireccion.setVisible(false);
        this.vista.btn_add.addActionListener(this);
        this.vista.btn_remove.addActionListener(this);
        this.vista.btn_update.addActionListener(this);

        llenar(this.vista.Tabla_estados);
        seleccionarTabla(this.vista.Tabla_estados); 
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
            if (!this.vista.campo_direccion.getText().isEmpty()) {
                this.modelo.setId(0);
                this.modelo.setDireccion(this.vista.campo_direccion.getText());

                if (!sql.VerificarDirecc(modelo).equals(this.vista.campo_direccion.getText())) {
                    if (sql.INSERT(modelo)) {
                        JOptionPane.showMessageDialog(null, "Dirección registrado.");
                        llenar(this.vista.Tabla_estados);
                        this.vista.campo_direccion.setText(null);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ya se ha registrado la dirección ingresado");
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
            if (!vista.campo_direccion.getText().isEmpty()) {
                modelo.setDireccion(vista.campo_direccion.getText());
                if (sql.getIdDirecc(modelo) != -1) {

                    int idObtenido = sql.getIdDirecc(modelo);

                    String resp = JOptionPane.showInputDialog("Ingrese nuevo numero");

                    if (!sql.VerificarDirecc(modelo).equals(resp)) {

                        if (resp != null && !"".equals(resp)) {
                            if (sql.UPDATE(resp, idObtenido)) {
                                JOptionPane.showMessageDialog(null, "Actualizado");
                                llenar(this.vista.Tabla_estados);
                                this.vista.campo_direccion.setText(null);

                            } else {
                                JOptionPane.showMessageDialog(null, "Error");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Recuerde... Para actualizar ingrese contenido");
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "La dirección ingresda ya esta registrada");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No existe la dirección ingresada");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar la dirección");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }
    
    private void remove() {
        try {
            if (!vista.campo_direccion.getText().isEmpty()) {
                modelo.setDireccion(vista.campo_direccion.getText());
                if (sql.VerificarDirecc(modelo).equals(modelo.getDireccion())) {

                    int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este registro?");
                    if (opSelect == JOptionPane.YES_OPTION) { 
                        if (sql.REMOVE(modelo)) {
                            JOptionPane.showMessageDialog(null, "Dirección eliminado");
                            llenar(this.vista.Tabla_estados);
                            this.vista.campo_direccion.setText(null);
                        } else {
                            JOptionPane.showMessageDialog(null, "error");
                        } 
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "La dirección no existe");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar la dirección");
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
                if (columnas == tabla.getColumnCount()) {
                    return true;
                } else {
                    return false;
                }
            }
        }; 
        tabla.setModel(modeloTabla);
        tabla.getTableHeader().setReorderingAllowed(false); 
        modeloTabla.addColumn("Dirección"); 
        Object[] colum = new Object[1];
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getDireccion(); 
            modeloTabla.addRow(colum);
        }

        return true;
    } 

    private void seleccionarTabla(JTable tb) {
        tb.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tb.getSelectedRow();
                String dir = tb.getValueAt(fila, 0).toString();
                modelo.setDireccion(dir);

                if (sql.VerificarDirecc(modelo).equals(dir)) {
                    System.out.println(modelo);
                    vista.campo_direccion.setText(String.valueOf(modelo.getDireccion()));
                }
            }
        });
    }
 
}

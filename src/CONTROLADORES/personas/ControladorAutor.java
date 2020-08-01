/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.personas;

import MODELOS.Helpers;
import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER; 
import MODELOS.MODELOCLASES.libros.Autor;
import MODELOS.SQL.libro.AutorSql;
import VISTAS.libros.GestionarAutor;
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
public class ControladorAutor implements ActionListener {

    private Autor modelo;
    private AutorSql sql;
    private GestionarAutor vista;

    public ControladorAutor(Autor modelo, AutorSql sql, GestionarAutor vista) {
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
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de Autores");

        llenar(this.vista.Tabla);
        seleccionarTabla(this.vista.Tabla);
 
        Helpers.soloLetras(this.vista.campo_nombre);
        Helpers.soloLetras(this.vista.campo_apellidoP);
        Helpers.soloLetras(this.vista.campo_apellidoM);
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

    private void add() {
        try {
            if (validaCampos()) {
                this.modelo.setId(0);
                this.modelo.setNombre(this.vista.campo_nombre.getText());
                this.modelo.setApellido_p(this.vista.campo_apellidoP.getText());
                this.modelo.setApellido_m(this.vista.campo_apellidoM.getText());
                
                String dato = modelo.getNombre()+""+modelo.getApellido_p()+""+modelo.getApellido_m();

                if (!sql.Verificar(modelo).equals(dato)) {
                    if (sql.INSERT(modelo)) {
                        JOptionPane.showMessageDialog(null, "Autor registrado.");
                        llenar(this.vista.Tabla);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ya se ha registrado al Autor");
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
                modelo.setNombre(vista.campo_nombre.getText());
                modelo.setApellido_p(vista.campo_apellidoP.getText());
                modelo.setApellido_m(vista.campo_apellidoM.getText());
                
                String dato = modelo.getNombre()+""+modelo.getApellido_p()+""+modelo.getApellido_m();

                System.out.println("Dato java: "+dato);
                
                if (sql.Verificar(modelo).equals(dato)) {

                    int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este registro?");
                    if (opSelect == JOptionPane.YES_OPTION) {

                        if (sql.REMOVE(modelo)) {
                            JOptionPane.showMessageDialog(null, "Autor eliminada");
                            llenar(this.vista.Tabla);
                            limpiar();
                        } else {
                            JOptionPane.showMessageDialog(null, "error");
                        }

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "El Autor no existe");
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
                modelo.setNombre(vista.campo_nombre.getText());
                modelo.setApellido_p(vista.campo_apellidoP.getText());
                modelo.setApellido_m(vista.campo_apellidoM.getText());
                if (sql.getIdAutor(modelo) != -1) {

                    int idObtenido = sql.getIdAutor(modelo);
                    String respNombre = JOptionPane.showInputDialog("Ingrese nuevo nombre", modelo.getNombre()); 
                    
                    if (respNombre != null && !"".equals(respNombre)) {
                        if (sql.updateNombre(respNombre, idObtenido)) {
                            JOptionPane.showMessageDialog(null, "Nombre actualizado");
                            llenar(this.vista.Tabla); 
                        } else {
                            JOptionPane.showMessageDialog(null, "Error");
                        }
                    } 
                    
                    String respApeP = JOptionPane.showInputDialog("Ingrese nuevo Apellido paterno", modelo.getApellido_p()); 
                    
                    if (respApeP != null && !"".equals(respApeP)) {
                        if (sql.updateApellidoP(respApeP, idObtenido)) {
                            JOptionPane.showMessageDialog(null, "Apellido paterno actualizado");
                            llenar(this.vista.Tabla); 
                        } else {
                            JOptionPane.showMessageDialog(null, "Error");
                        }
                    } 
                    
                    String respApeM = JOptionPane.showInputDialog("Ingrese nuevo Apellido materno", modelo.getApellido_m()); 
                    
                    if (respApeM != null && !"".equals(respApeM)) {
                        if (sql.updateApellidoM(respApeM, idObtenido)) {
                            JOptionPane.showMessageDialog(null, "Apellido materno actualizado");
                            llenar(this.vista.Tabla); 
                        } else {
                            JOptionPane.showMessageDialog(null, "Error");
                        }
                    } 
                    
                } else {
                    JOptionPane.showMessageDialog(null, "No existe el autor ingresado");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usted debe llenar los campos");
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
                if (columnas == 3) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        tabla.setModel(modeloTabla);
        tabla.getTableHeader().setReorderingAllowed(false);

        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellido Paterno");
        modeloTabla.addColumn("Apellido Materno");

        Object[] colum = new Object[3];
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getNombre();
            colum[1] = sql.listar().get(i).getApellido_p();
            colum[2] = sql.listar().get(i).getApellido_m();

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
                String ape_p = tb.getValueAt(fila, 1).toString();
                String ape_m = tb.getValueAt(fila, 2).toString();
                modelo.setNombre(nombre);
                modelo.setApellido_p(ape_p);
                modelo.setApellido_m(ape_m);
                
                String datoTabla = nombre+""+ape_p+""+ape_m;

                if (sql.Verificar(modelo).equals(datoTabla)) {
                    vista.campo_nombre.setText(String.valueOf(modelo.getNombre()));
                    vista.campo_apellidoP.setText(String.valueOf(modelo.getApellido_p()));
                    vista.campo_apellidoM.setText(String.valueOf(modelo.getApellido_m()));
                }
            }
        });
    }
    
    public boolean validaCampos(){
       boolean est = true;
       
       if(this.vista.campo_nombre.getText().isEmpty()){
           est = false;
       }
       
       if(this.vista.campo_apellidoP.getText().isEmpty()){
           est = false;
       }
       if(this.vista.campo_apellidoM.getText().isEmpty()){
           est = false;
       }
       
       return est;
    }
    
    public void limpiar(){
        this.vista.campo_nombre.setText(null);
        this.vista.campo_apellidoM.setText(null);
        this.vista.campo_apellidoP.setText(null);
    }

}

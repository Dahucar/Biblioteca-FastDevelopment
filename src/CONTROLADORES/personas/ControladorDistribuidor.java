/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.personas;

import MODELOS.Helpers;
import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER;
import MODELOS.MODELOCLASES.personas.Distribuidor;
import MODELOS.MODELOCLASES.personas.datos.Direccion;
import MODELOS.MODELOCLASES.personas.datos.Telefono;
import MODELOS.SQL.personas.DistribuidorSql;
import VISTAS.personas.GestionarDistribuidores;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Daniel Huenul
 */
public class ControladorDistribuidor implements ActionListener {

    private Distribuidor modelo;
    private DistribuidorSql sql;
    private GestionarDistribuidores vista;

    public ControladorDistribuidor(Distribuidor modelo, DistribuidorSql sql, GestionarDistribuidores vista) {
        this.modelo = modelo;
        this.sql = sql;
        this.vista = vista;

        this.vista.Submenu_salir.addActionListener(this);
        this.vista.Submenu_volver.addActionListener(this);
        this.vista.btn_add.addActionListener(this);
        this.vista.btn_remove.addActionListener(this);
        this.vista.btn_update.addActionListener(this);
        this.vista.btn_clear.addActionListener(this);

        muestrate();
    }

    public void muestrate() {
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de distribuidores");

        llenar(this.vista.tabla);
        seleccionarTabla(this.vista.tabla);

        Helpers.soloLetras(this.vista.campo_nombre);

        Helpers.cargarComboDirecciones(this.vista.como_deireccion);
        Helpers.cargarComboNumeros(this.vista.combo_numtelefono);

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
        modeloTabla.addColumn("RUT / DNI");
        modeloTabla.addColumn("Nombre empresa");
        modeloTabla.addColumn("Año de sociedad");
        modeloTabla.addColumn("N° Telefonico");
        modeloTabla.addColumn("Dirección");

        Object[] colum = new Object[7];
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getId();
            colum[1] = sql.listar().get(i).getRut();
            colum[2] = sql.listar().get(i).getNombre_empresa();
            colum[3] = sql.listar().get(i).getAnno_asociadad();
            colum[4] = sql.listar().get(i).getTelefono().getNumero();
            colum[5] = sql.listar().get(i).getDireccion().getDireccion();

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
                    vista.campo_rut.setText(modelo.getRut());
                    vista.campo_nombre.setText(String.valueOf(modelo.getNombre_empresa()));
                    vista.calendario_fechasociedad.setDate(modelo.getAnno_asociadad());

                    ComboBoxModel modeloCombo = vista.combo_numtelefono.getModel();
                    for (int i = 0; i < modeloCombo.getSize(); i++) {
                        if (modeloCombo.getElementAt(i).toString().equals(modelo.getTelefono().getNumero())) {
                            vista.combo_numtelefono.setSelectedIndex(i);
                            break;
                        }
                    }

                    ComboBoxModel modeloCombov = vista.como_deireccion.getModel();
                    for (int i = 0; i < modeloCombov.getSize(); i++) {
                        if (modeloCombov.getElementAt(i).toString().equals(modelo.getDireccion().getDireccion())) {
                            vista.como_deireccion.setSelectedIndex(i);
                            break;
                        }
                    }

                    System.out.println(modelo.mostrarDatos());

                }
            }
        });
    }

    private void add() {
        try {
            if (validaCampos()) {
                if (Helpers.validarRut(this.vista.campo_rut.getText())) { 
                    this.modelo.setId(modelo.getId());
                    this.modelo.setRut(this.vista.campo_rut.getText());
                    this.modelo.setNombre_empresa(this.vista.campo_nombre.getText()); 
                    Date date = (Date) vista.calendario_fechasociedad.getDate();
                    long d = date.getTime();
                    java.sql.Date fecha = new java.sql.Date(d); 
                    this.modelo.setAnno_asociadad(fecha);
                    this.modelo.setDireccion((Direccion) this.vista.como_deireccion.getSelectedItem());
                    this.modelo.setTelefono((Telefono) this.vista.combo_numtelefono.getSelectedItem()); 
 

                    System.out.println(this.modelo.toString());

                    if (sql.VerificarRut(modelo) == 0) {
                        if (sql.INSERT(modelo)) {
                            JOptionPane.showMessageDialog(null, "Distribuidor registrado.");
                            llenar(this.vista.tabla);
                            limpiar();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Ya se ha registrado al un distribuidor con ese rut");
                    }
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
                        JOptionPane.showMessageDialog(null, "Distribuidor eliminado");
                        llenar(this.vista.tabla);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "error");
                    } 
                } 
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione el registro de la tabla de datos");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }

    private void update() {
        try {
            if (validaCampos()) {
                if (Helpers.validarRut(this.vista.campo_rut.getText())) {
                    this.modelo.setId(modelo.getId());
                    this.modelo.setRut(this.vista.campo_rut.getText());
                    this.modelo.setNombre_empresa(this.vista.campo_nombre.getText()); 
                    Date date = (Date) vista.calendario_fechasociedad.getDate();
                    long d = date.getTime();
                    java.sql.Date fecha = new java.sql.Date(d); 
                    this.modelo.setAnno_asociadad(fecha);
                    this.modelo.setDireccion((Direccion) this.vista.como_deireccion.getSelectedItem());
                    this.modelo.setTelefono((Telefono) this.vista.combo_numtelefono.getSelectedItem()); 
                    
                    int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea actualizar este registro?");
                    if (opSelect == JOptionPane.YES_OPTION) {
                        if (sql.UPDATE(modelo)) {
                            JOptionPane.showMessageDialog(null, "Distribuidor actualizado");
                            llenar(this.vista.tabla);
                            limpiar();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Formato de rut invalido");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }

    private void limpiar() {
        this.vista.calendario_fechasociedad.setDate(null);
        this.vista.campo_nombre.setText(null);
        this.vista.campo_rut.setText(null);
        this.vista.combo_numtelefono.setSelectedIndex(0);
        this.vista.como_deireccion.setSelectedIndex(0);
    }

    private boolean validaCampos() {
        boolean est = true;
        if(this.vista.campo_nombre.getText().isEmpty()){est=false;}
        if(this.vista.campo_rut.getText().isEmpty()){est=false;} 
        if(this.vista.combo_numtelefono.getSelectedIndex() == 0){est=false;}
        if(this.vista.como_deireccion.getSelectedIndex() == 0){est=false;}
        return est;
    }
}

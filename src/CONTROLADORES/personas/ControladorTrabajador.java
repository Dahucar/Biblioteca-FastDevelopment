/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.personas;

import CONTROLADORES.personas.asociar.asociar_trabajador.ControladorAsociarCorreoTrabajador;
import CONTROLADORES.personas.asociar.asociar_trabajador.ControladorAsociarDireccionTrabajador;
import CONTROLADORES.personas.asociar.asociar_trabajador.ControladorAsociarTelefonoTrabajador;
import MODELOS.Helpers;
import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER;
import MODELOS.MODELOCLASES.personas.Trabajador;
import MODELOS.MODELOCLASES.personas.datos.Correo;
import MODELOS.MODELOCLASES.personas.datos.Direccion;
import MODELOS.MODELOCLASES.personas.datos.Telefono;
import MODELOS.SQL.personas.TrabajadorSql;
import MODELOS.SQL.personas.asociar.AsociarCorreoSql;
import MODELOS.SQL.personas.asociar.AsociarDireccionSql;
import MODELOS.SQL.personas.asociar.AsociarNumeroSql;
import VISTAS.personas.AsociarCorreos;
import VISTAS.personas.AsociarDireccion;
import VISTAS.personas.AsociarNumero;
import VISTAS.personas.GestionarTrabajadores;
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
public class ControladorTrabajador implements ActionListener {

    private Trabajador modelo;
    private TrabajadorSql sql;
    private GestionarTrabajadores vista;

    public ControladorTrabajador(Trabajador modelo, TrabajadorSql sql, GestionarTrabajadores vista) {
        this.modelo = modelo;
        this.sql = sql;
        this.vista = vista;
        this.vista.Submenu_salir.addActionListener(this);
        this.vista.Submenu_volver.addActionListener(this);
        this.vista.btn_add.addActionListener(this);
        this.vista.btn_remove.addActionListener(this);
        this.vista.btn_update.addActionListener(this);
        this.vista.btn_clear.addActionListener(this);
        this.vista.btn_asociarCorreos.addActionListener(this);
        this.vista.btn_asociarTelefonos.addActionListener(this);
        this.vista.btn_asociarDireccion.addActionListener(this);

        muestrate();
    }

    public void muestrate() {
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de trabajadores");

        llenar(this.vista.tabla);
        seleccionarTabla(this.vista.tabla);
// 
        Helpers.soloLetras(this.vista.campo_nombre);
        Helpers.soloLetras(this.vista.campo_apellidoP);
        Helpers.soloLetras(this.vista.campo_apellidoM);
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

        if (ae.getSource().equals(this.vista.btn_clear)) {
            limpiar();
        }

        if (ae.getSource().equals(this.vista.btn_asociarCorreos)) {
            asociarCorreo();
        }

        if (ae.getSource().equals(this.vista.btn_asociarTelefonos)) {
            asociarTelefono();
        }

        if (ae.getSource().equals(this.vista.btn_asociarDireccion)) {
            asociarDireccion();
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

        modeloTabla.addColumn("Rut");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellido P.");
        modeloTabla.addColumn("Apellido m.");

        Object[] colum = new Object[5];
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getRut();
            colum[1] = sql.listar().get(i).getNombre();
            colum[2] = sql.listar().get(i).getApellido_p();
            colum[3] = sql.listar().get(i).getApellido_m();

            modeloTabla.addRow(colum);
        }

        return true;
    }

    private void seleccionarTabla(JTable tb) {
        tb.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tb.getSelectedRow();
                String rut = tb.getValueAt(fila, 0).toString();
                modelo.setRut(rut);

                if (sql.SELECT(modelo)) {
                    vista.campo_rut.setText(modelo.getRut());
                    vista.campo_nombre.setText(modelo.getNombre());
                    vista.campo_apellidoP.setText(modelo.getApellido_p());
                    vista.campo_apellidoM.setText(modelo.getApellido_m());
                }

                System.out.println(modelo.mostrarDatos());
            }
        });
    }

    private void add() {
        try {
            if (validaCampos()) {
                if (Helpers.validarRut(this.vista.campo_rut.getText())) {

                    this.modelo.setId(0);
                    this.modelo.setRut(this.vista.campo_rut.getText());
                    this.modelo.setNombre(this.vista.campo_nombre.getText());
                    this.modelo.setApellido_p(this.vista.campo_apellidoP.getText());
                    this.modelo.setApellido_m(this.vista.campo_apellidoM.getText());

                    System.out.println(this.modelo.toString());

                    if (!sql.verificar(this.modelo.getRut())) {
                        if (sql.INSERT(modelo)) {
                            JOptionPane.showMessageDialog(null, "Trabajador registrado.");
                            llenar(this.vista.tabla);
                            limpiar();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Rut asignado a otro trabajador.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Rut invalido");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usted debe llenar los campos");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }

    private void remove() {
        try {
            if (validaCampos()) {
                modelo.setRut(vista.campo_rut.getText());

                int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este registro?");
                if (opSelect == JOptionPane.YES_OPTION) {
                    if (sql.REMOVE(modelo)) {
                        JOptionPane.showMessageDialog(null, "Trabajador eliminado");
                        llenar(this.vista.tabla);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "error");
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

    private void update() {
        try {
            if (validaCampos()) {
                modelo.setId(modelo.getId());
                modelo.setRut(vista.campo_rut.getText());
                modelo.setNombre(vista.campo_nombre.getText());
                modelo.setApellido_p(vista.campo_apellidoP.getText());
                modelo.setApellido_m(vista.campo_apellidoM.getText());

                int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea actualizar este registro?");
                if (opSelect == JOptionPane.YES_OPTION) {

                    if (sql.UPDATE(modelo)) {
                        JOptionPane.showMessageDialog(null, "Trabajador actualizado");
                        llenar(this.vista.tabla);
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
        this.vista.campo_apellidoM.setText(null);
        this.vista.campo_apellidoP.setText(null);
        this.vista.campo_nombre.setText(null);
        this.vista.campo_rut.setText(null);
    }

    private boolean validaCampos() {
        boolean est = true;
        if (this.vista.campo_rut.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.campo_nombre.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.campo_apellidoP.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.campo_apellidoM.getText().isEmpty()) {
            est = false;
        }
        return est;
    }

    private void asociarCorreo() {
        try {
            if (validaCampos()) {
                Correo corr = new Correo();
                AsociarCorreos ass = new AsociarCorreos();
                AsociarCorreoSql assSql = new AsociarCorreoSql();
                ControladorAsociarCorreoTrabajador control = new ControladorAsociarCorreoTrabajador(modelo, corr, ass, assSql);
                vista.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un libro de la tabla");
            }
        } catch (Exception e) {
            System.err.println("asociarCorreo: " + e);
        }
    }

    private void asociarTelefono() {
        try {
            if (validaCampos()) {
                Telefono corr = new Telefono();
                AsociarNumero ass = new AsociarNumero();
                AsociarNumeroSql assSql = new AsociarNumeroSql();
                ControladorAsociarTelefonoTrabajador control = new ControladorAsociarTelefonoTrabajador(modelo, corr, ass, assSql);
                vista.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un libro de la tabla");
            }
        } catch (Exception e) {
            System.err.println("asociarCorreo: " + e);
        }
    }

    private void asociarDireccion() {
        try {
            if (validaCampos()) {
                Direccion corr = new Direccion();
                AsociarDireccion ass = new AsociarDireccion();
                AsociarDireccionSql assSql = new AsociarDireccionSql();
                ControladorAsociarDireccionTrabajador control = new ControladorAsociarDireccionTrabajador(modelo, corr, ass, assSql);
                vista.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un libro de la tabla");
            }
        } catch (Exception e) {
            System.err.println("asociarCorreo: " + e);
        }
    }

}

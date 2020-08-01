/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.personas;

import CONTROLADORES.personas.asociar.asociar_cliente.ControladorAsociarCorreoCliente;
import CONTROLADORES.personas.asociar.asociar_cliente.ControladorAsociarDireccionCliente;
import CONTROLADORES.personas.asociar.asociar_cliente.ControladorAsociarTelefonoCliente;
import MODELOS.Helpers;
import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER;
import MODELOS.MODELOCLASES.personas.Cliente;
import MODELOS.MODELOCLASES.personas.datos.Correo;
import MODELOS.MODELOCLASES.personas.datos.Direccion;
import MODELOS.MODELOCLASES.personas.datos.Telefono;
import MODELOS.SQL.personas.ClienteSql;
import MODELOS.SQL.personas.asociar.AsociarCorreoSql;
import MODELOS.SQL.personas.asociar.AsociarDireccionSql;
import MODELOS.SQL.personas.asociar.AsociarNumeroSql;
import VISTAS.personas.AsociarCorreos;
import VISTAS.personas.AsociarDireccion;
import VISTAS.personas.AsociarNumero;
import VISTAS.personas.GestionarClientes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Daniel Huenul
 */
public class ControladorCliente implements ActionListener {

    private Cliente modelo;
    private ClienteSql sql;
    private GestionarClientes vista;

    public ControladorCliente(Cliente modelo, ClienteSql sql, GestionarClientes vista) {
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
        this.vista.btn_asociarDireccion.addActionListener(this);
        this.vista.btn_asociarTelefonos.addActionListener(this);

        muestrate();
    }

    public void muestrate() {
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de clientes");

        llenar(this.vista.tabla);
        seleccionarTabla(this.vista.tabla);
//
//        buscarEnTabla(this.vista.campo_buscar, this.vista.Tabla_estados);
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

        if (ae.getSource().equals(this.vista.btn_asociarCorreos)) {
            asociarCorreo();
        }

        if (ae.getSource().equals(this.vista.btn_asociarDireccion)) {
            asociarDireccion();
        }

        if (ae.getSource().equals(this.vista.btn_asociarTelefonos)) {
            asociarTelefono();
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
        modeloTabla.addColumn("RUT");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellido P.");
        modeloTabla.addColumn("Apellido M.");
        modeloTabla.addColumn("Fecha de contrato");

        Object[] colum = new Object[7];
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getId();
            colum[1] = sql.listar().get(i).getRut();
            colum[2] = sql.listar().get(i).getNombre();
            colum[3] = sql.listar().get(i).getApellido_p();
            colum[4] = sql.listar().get(i).getApellido_m();
            colum[5] = sql.listar().get(i).getAnno_sociedad();

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
                    vista.campo_nombre.setText(modelo.getNombre());
                    vista.campo_apellidoP.setText(modelo.getApellido_p());
                    vista.campo_apellidoM.setText(modelo.getApellido_m());
                    vista.calendario_fechacontrato.setDate(modelo.getAnno_sociedad());
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
                    this.modelo.setNombre(this.vista.campo_nombre.getText());
                    this.modelo.setApellido_p(this.vista.campo_apellidoP.getText());
                    this.modelo.setApellido_m(this.vista.campo_apellidoM.getText());

                    Date date = (Date) vista.calendario_fechacontrato.getDate();
                    long d = date.getTime();
                    java.sql.Date fecha = new java.sql.Date(d);

                    this.modelo.setAnno_sociedad(fecha);

                    System.out.println(this.modelo.toString());

                    if (sql.VerificarRut(modelo) == 0) {
                        if (sql.INSERT(modelo)) {
                            JOptionPane.showMessageDialog(null, "Cliente registrado.");
                            llenar(this.vista.tabla);
                            limpiar();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Ya se ha registrado al cliente");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Formato de rut invalido");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usted debe llenar los campos");
            }

        } catch (Exception e) {
            System.err.println("err add-> " + e);
            JOptionPane.showMessageDialog(null, "Error detectado");
        }
    }

    private void remove() {
        try {
            if (validaCampos()) {

                int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este registro?");
                if (opSelect == JOptionPane.YES_OPTION) {

                    if (sql.REMOVE(modelo)) {
                        JOptionPane.showMessageDialog(null, "Cliente eliminado");
                        llenar(this.vista.tabla);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "error");
                    }

                }

            } else {
                JOptionPane.showMessageDialog(null, "Debe seleccionar el cliente de la tabla");
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
                    modelo.setId(modelo.getId());
                    modelo.setRut(vista.campo_rut.getText());
                    modelo.setNombre(vista.campo_nombre.getText());
                    modelo.setApellido_p(vista.campo_apellidoP.getText());
                    modelo.setApellido_m(vista.campo_apellidoM.getText());
                    Date date = (Date) vista.calendario_fechacontrato.getDate();
                    long d = date.getTime();
                    java.sql.Date fecha = new java.sql.Date(d);
                    System.err.println("modelo cliente -> " + modelo.toString());
                    modelo.setAnno_sociedad(fecha);
                    int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea actualizar este registro?");
                    if (opSelect == JOptionPane.YES_OPTION) {
                        if (sql.UPDATE(modelo)) {
                            JOptionPane.showMessageDialog(null, "Cliente actualizado");
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

    private void asociarCorreo() {
        try {
            if (validaCampos()) {
                Correo modelCorreo = new Correo();
                AsociarCorreos vistaModel = new AsociarCorreos();
                AsociarCorreoSql sqlModel = new AsociarCorreoSql();
                ControladorAsociarCorreoCliente control = new ControladorAsociarCorreoCliente(modelo, modelCorreo, vistaModel, sqlModel);
                this.vista.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un cliente de la tabla");
            }
        } catch (Exception e) {
            System.err.println("asociarCorreo: " + e);
        }
    }

    private void asociarDireccion() {
        try {
            if (validaCampos()) {
                Direccion modelCorreo = new Direccion();
                AsociarDireccion vistaModel = new AsociarDireccion();
                AsociarDireccionSql sqlModel = new AsociarDireccionSql();
                ControladorAsociarDireccionCliente control = new ControladorAsociarDireccionCliente(modelo, modelCorreo, vistaModel, sqlModel);
                this.vista.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un cliente de la tabla");
            }
        } catch (Exception e) {
            System.err.println("asociarCorreo: " + e);
        }
    }

    private void asociarTelefono() {
        try {
            if (validaCampos()) {
                Telefono modelCorreo = new Telefono();
                AsociarNumero vistaModel = new AsociarNumero();
                AsociarNumeroSql sqlModel = new AsociarNumeroSql();
                ControladorAsociarTelefonoCliente control = new ControladorAsociarTelefonoCliente(modelo, modelCorreo, vistaModel, sqlModel);
                this.vista.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un cliente de la tabla");
            }
        } catch (Exception e) {
            System.err.println("asociarCorreo: " + e);
        }
    }

    private void limpiar() {
        this.vista.calendario_fechacontrato.setDate(null);
        this.vista.campo_apellidoM.setText(null);
        this.vista.campo_apellidoP.setText(null);
        this.vista.campo_rut.setText(null);
        this.vista.campo_nombre.setText(null);
    }

    private boolean validaCampos() {
        boolean est = true;
        if (this.vista.campo_apellidoM.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.campo_apellidoP.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.campo_nombre.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.campo_rut.getText().isEmpty()) {
            est = false;
        }
        return est;
    }
}

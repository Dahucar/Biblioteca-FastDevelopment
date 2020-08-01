/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.libros;

import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER; 
import static MODELOS.Helpers.cargarComboLibros;
import static MODELOS.Helpers.soloLetras;
import MODELOS.MODELOCLASES.libros.Idioma;
import MODELOS.MODELOCLASES.libros.Libro;
import MODELOS.SQL.libro.IdiomaSql;
import VISTAS.libros.GestionarIdioma;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Daniel Huenul
 */
public class ControladorIdioma implements ActionListener {

    private Idioma modelo;
    private IdiomaSql sql;
    private GestionarIdioma vista;

    private Libro libTem = new Libro();

    public ControladorIdioma(Idioma modelo, IdiomaSql sql, GestionarIdioma vista) {
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
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de idiomas");

        llenar(this.vista.tabla);
        seleccionarTabla(this.vista.tabla);

        soloLetras(this.vista.campo_idioma);
        cargarComboLibros(this.vista.combo_libros);
 
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

        modeloTabla.addColumn("Idioma");
        modeloTabla.addColumn("N° serie libro");
        modeloTabla.addColumn("Nombre libro");

        Object[] colum = new Object[3];
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getIdioma();
            colum[1] = sql.listar().get(i).getIdlibro().getNumSerie();
            colum[2] = sql.listar().get(i).getIdlibro().getNombre();

            modeloTabla.addRow(colum);
        }

        return true;
    }

    private void seleccionarTabla(JTable tb) {
        tb.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tb.getSelectedRow();
                String idioma = tb.getValueAt(fila, 0).toString();
                int num_Serie = Integer.parseInt(tb.getValueAt(fila, 1).toString());
                String nombrelib = tb.getValueAt(fila, 2).toString();

                modelo.setIdioma(idioma);

                Libro lib = new Libro();
                lib.setNumSerie(num_Serie);
                lib.setNombre(nombrelib);

                modelo.setIdlibro(lib);

                String dato = modelo.getIdioma() + "" + modelo.getIdlibro().getNumSerie();

                if (sql.verificarRegistro(modelo).equals(dato)) {
                    vista.campo_idioma.setText(idioma);

                    //combo de libros
                    ComboBoxModel modeloCombo = vista.combo_libros.getModel();
                    for (int i = 0; i < modeloCombo.getSize(); i++) {

                        if (modeloCombo.getElementAt(i).toString().equals(modelo.getIdlibro().getNombre())) {
                            vista.combo_libros.setSelectedIndex(i);

                            libTem = (Libro) vista.combo_libros.getSelectedItem();
                            

                            break;
                        }

                    }

                }
            }
        });
    }

    private void add() {
        try {
            if (valida()) {
                this.modelo.setId(0);
                this.modelo.setIdioma(vista.campo_idioma.getText());
                this.modelo.setIdlibro((Libro) vista.combo_libros.getSelectedItem());

                if (!sql.verificar(modelo)) {
                    if (sql.INSERT(modelo)) {
                        JOptionPane.showMessageDialog(null, "Estado registrado.");
                        llenar(this.vista.tabla);
                        this.vista.campo_idioma.setText(null);
                        this.vista.combo_libros.setSelectedIndex(0);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ya se ha registrado el idioma");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
            }

        } catch (Exception e) {
            System.err.println("addestado: " + e);
        }
    }

    private void remove() {
        try {
            if (valida()) {
                modelo.setIdioma(vista.campo_idioma.getText());
                modelo.setIdlibro(libTem);

                String dato = modelo.getIdioma() + "" + modelo.getIdlibro().getNumSerie();

                System.out.println("Dato java: " + dato);

                if (sql.verificarRegistro(modelo).equals(dato)) {

                    int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este registro?");
                    if (opSelect == JOptionPane.YES_OPTION) {

                        if (sql.REMOVE(modelo)) {
                            JOptionPane.showMessageDialog(null, "Idioma eliminado");
                            llenar(this.vista.tabla);
                            this.vista.campo_idioma.setText(null);
                            this.vista.combo_libros.setSelectedIndex(0);
                            this.libTem = null;
                        } else {
                            JOptionPane.showMessageDialog(null, "error");
                        }

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "El idioma no existe");
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
            if (valida()) {
                modelo.setIdioma(vista.campo_idioma.getText());
                modelo.setIdlibro((Libro) vista.combo_libros.getSelectedItem());
                if (sql.verificar(modelo)) {

                    System.out.println("__________________________________");
                    System.out.println(modelo.getIdlibro().mostrarDatos());

                    String resp = JOptionPane.showInputDialog("Ingrese nuevo idioma", modelo.getIdioma());

                    if (resp != null && !"".equals(resp)) {
                        if (sql.update(resp, modelo)) {
                            JOptionPane.showMessageDialog(null, "Idioma actualizado");
                            llenar(this.vista.tabla);
                        } else {
                            JOptionPane.showMessageDialog(null, "Error");
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "No existe el idima ingresado");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usted debe llenar los campos");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }

    private boolean valida() {
        boolean est = true;

        if (this.vista.campo_idioma.getText().isEmpty()) {
            est = false;
        }

        if (this.vista.combo_libros.getSelectedIndex() == 0) {
            est = false;
        }

        return est;

    }

}

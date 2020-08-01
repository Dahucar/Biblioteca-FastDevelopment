/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.libros;

import MODELOS.Helpers;
import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER; 
import MODELOS.MODELOCLASES.libros.Autor;
import MODELOS.MODELOCLASES.libros.Editorial;
import MODELOS.MODELOCLASES.libros.Estado;
import MODELOS.MODELOCLASES.libros.Libro;
import MODELOS.SQL.libro.AsociarAutorSql; 
import MODELOS.SQL.libro.LibroSql;
import VISTAS.libros.AsociarAutor;
import VISTAS.libros.GestionarLibro;
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
public class ControladorLibro implements ActionListener {

    private Libro modelo;
    private LibroSql sql;
    private GestionarLibro vista; 

    public ControladorLibro(Libro modelo, LibroSql sql, GestionarLibro vista) {
        this.modelo = modelo;
        this.sql = sql;
        this.vista = vista;
        this.vista.Submenu_salir.addActionListener(this);
        this.vista.Submenu_volver.addActionListener(this);
        this.vista.btn_add.addActionListener(this);
        this.vista.btn_remove.addActionListener(this);
        this.vista.btn_update.addActionListener(this);
        this.vista.btn_clear.addActionListener(this);
        this.vista.btn_asociarAutor.addActionListener(this);

        muestrate();
    }

    public void muestrate() {
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de libros");

        llenar(this.vista.Tabla);
        seleccionarTabla(this.vista.Tabla);
 

        Helpers.soloNumeros(this.vista.campo_numSerie);
        Helpers.soloNumeros(this.vista.campo_isbn);
        Helpers.soloNumeros(this.vista.campo_numPaginas);
        Helpers.soloNumeros(this.vista.campo_precio);

        Helpers.cargarComboEstados(this.vista.como_estado);
        Helpers.cargarComboEditoriales(this.vista.combo_editoriales);

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

        if (ae.getSource().equals(this.vista.btn_asociarAutor)) {
            asociarAutor();
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
        
        modeloTabla.addColumn("N° Serie");
        modeloTabla.addColumn("ISBN");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("N° Paginas");
        modeloTabla.addColumn("Precio CLP");
        modeloTabla.addColumn("Stock");
        modeloTabla.addColumn("Nombre Editorial");
        modeloTabla.addColumn("Estado");

        Object[] colum = new Object[8];
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getNumSerie();
            colum[1] = sql.listar().get(i).getIsbn();
            colum[2] = sql.listar().get(i).getNombre();
            colum[3] = sql.listar().get(i).getNumPaginas();
            colum[4] = sql.listar().get(i).getPrecio();
            colum[5] = sql.listar().get(i).getStock();
            colum[6] = sql.listar().get(i).getEditorial().getNombre();
            colum[7] = sql.listar().get(i).getEstado().getEstado();

            modeloTabla.addRow(colum);
        }

        return true;
    }

    private void seleccionarTabla(JTable tb) {
        tb.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tb.getSelectedRow();
                int numSerie = Integer.parseInt(tb.getValueAt(fila, 0).toString());
                modelo.setNumSerie(numSerie);

                if (sql.SELECT(modelo)) {
                    vista.campo_nombre.setText(modelo.getNombre());
                    vista.campo_numSerie.setText(String.valueOf(modelo.getNumSerie()));
                    vista.campo_isbn.setText(String.valueOf(modelo.getIsbn()));
                    vista.campo_numPaginas.setText(String.valueOf(modelo.getNumPaginas()));
                    vista.campo_precio.setText(String.valueOf(modelo.getPrecio()));
                    vista.campo_stock.setText(String.valueOf(modelo.getStock()));

                    ComboBoxModel modeloCombo = vista.combo_editoriales.getModel();
                    for (int i = 0; i < modeloCombo.getSize(); i++) {
                        if (modeloCombo.getElementAt(i).toString().equals(modelo.getEditorial().getNombre())) {
                            vista.combo_editoriales.setSelectedIndex(i);
                            break;
                        }
                    }

                    ComboBoxModel modeloCombov = vista.como_estado.getModel();
                    for (int i = 0; i < modeloCombov.getSize(); i++) {
                        if (modeloCombov.getElementAt(i).toString().equals(modelo.getEstado().getEstado())) {
                            vista.como_estado.setSelectedIndex(i);
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

                this.modelo.setId(modelo.getId());
                this.modelo.setNumSerie(Integer.parseInt(this.vista.campo_numSerie.getText()));
                this.modelo.setIsbn(Integer.parseInt(this.vista.campo_isbn.getText()));
                this.modelo.setNombre(this.vista.campo_nombre.getText());
                this.modelo.setNumPaginas(Integer.parseInt(this.vista.campo_numPaginas.getText()));
                this.modelo.setPrecio(Float.parseFloat(this.vista.campo_precio.getText()));
                this.modelo.setStock(Integer.parseInt(vista.campo_stock.getText()));

                Editorial editSelec = (Editorial) this.vista.combo_editoriales.getSelectedItem();
                Estado estSelec = (Estado) this.vista.como_estado.getSelectedItem();

                this.modelo.setEditorial(editSelec);
                this.modelo.setEstado(estSelec);

                System.out.println(this.modelo.toString());

                if (sql.Verificar(modelo) == 0) {
                    if (sql.INSERT(modelo)) {
                        JOptionPane.showMessageDialog(null, "Libro registrado.");
                        llenar(this.vista.Tabla);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ya se ha registrado al libro");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Usted debe llenar los campos");
            }

        } catch (Exception e) {
            System.err.println("addestado: " + e);
                JOptionPane.showMessageDialog(null, "Procure no ingresar letras para campos destinados a numeros");
        }
    }

    private void remove() {
        try {
            if (!vista.campo_numSerie.getText().isEmpty()) {
                modelo.setNumSerie(Integer.parseInt(vista.campo_numSerie.getText()));

                int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este registro?");
                if (opSelect == JOptionPane.YES_OPTION) {

                    if (sql.REMOVE(modelo)) {
                        JOptionPane.showMessageDialog(null, "Libro eliminado");
                        llenar(this.vista.Tabla);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "error");
                    }

                }

            } else {
                JOptionPane.showMessageDialog(null, "Debe ingrese el N° de serie del libro que deseea borrar");
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
                modelo.setNumSerie(Integer.parseInt(vista.campo_numSerie.getText()));
                modelo.setIsbn(Integer.parseInt(vista.campo_isbn.getText()));
                modelo.setNombre(vista.campo_nombre.getText());
                modelo.setNumPaginas(Integer.parseInt(vista.campo_numPaginas.getText()));
                modelo.setPrecio(Float.parseFloat(vista.campo_precio.getText()));
                modelo.setStock(Integer.parseInt(vista.campo_stock.getText()));

                Editorial editSelec = (Editorial) vista.combo_editoriales.getSelectedItem();
                Estado estSelec = (Estado) vista.como_estado.getSelectedItem();

                modelo.setEditorial(editSelec);
                modelo.setEstado(estSelec);

                int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea actualizar este registro?");
                if (opSelect == JOptionPane.YES_OPTION) {

                    if (sql.UPDATE(modelo)) {
                        JOptionPane.showMessageDialog(null, "Libro actualizado");
                        llenar(this.vista.Tabla);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error");
                    }
                    
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }

    private boolean validaCampos() {
        boolean est = true;

        if (this.vista.campo_nombre.getText().isEmpty()) {
            est = false;
        }

        if (this.vista.campo_isbn.getText().isEmpty()) {
            est = false;
        }

        if (this.vista.campo_numPaginas.getText().isEmpty()) {
            est = false;
        }

        if (this.vista.campo_numSerie.getText().isEmpty()) {
            est = false;
        }

        if (this.vista.campo_precio.getText().isEmpty()) {
            est = false;
        }

        if (this.vista.combo_editoriales.getSelectedIndex() == 0) {
            est = false;
        }

        if (this.vista.como_estado.getSelectedIndex() == 0) {
            est = false;
        }

        return est;
    }

    private void limpiar() {
        this.vista.campo_isbn.setText(null);
        this.vista.campo_nombre.setText(null);
        this.vista.campo_numPaginas.setText(null);
        this.vista.campo_numSerie.setText(null);
        this.vista.campo_precio.setText(null);
        this.vista.combo_editoriales.setSelectedIndex(0);
        this.vista.como_estado.setSelectedIndex(0);
    }

    private void asociarAutor() {
        try {
            
            if(validaCampos()){ 
                //el modelo ya esta lleno 
                Autor au = new Autor();
                AsociarAutor asoc = new AsociarAutor();
                AsociarAutorSql ausql = new AsociarAutorSql();
                ControladorAsociarAutor control = new ControladorAsociarAutor(au, modelo, asoc, ausql);
                this.vista.dispose();
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione un libro de la tabla");
            }
            
        } catch (Exception e) {
            System.err.println("asociarAutor: " + e);
        }
    }

}

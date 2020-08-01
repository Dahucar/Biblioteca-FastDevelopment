/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.libros;

import static MODELOS.Helpers.SALIR; 
import MODELOS.MODELOCLASES.libros.Autor;
import MODELOS.MODELOCLASES.libros.Libro;
import MODELOS.SQL.libro.AsociarAutorSql;
import MODELOS.SQL.libro.LibroSql;
import VISTAS.libros.AsociarAutor;
import VISTAS.libros.GestionarLibro;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Daniel Huenul
 */
public class ControladorAsociarAutor implements ActionListener {

    private Autor modeloAutor;
    private Libro modeloLibro;
    private AsociarAutor vista;
    private AsociarAutorSql sql;

    private Autor autTemp = null;
    private DefaultTableModel modeloTablaRescatado = null;

    /*
    SELECT lib.num_serie, lib.isbn, lib.nombre, aut.nombre, aut.apellido_p, aut.apellido_m 
    FROM autores_de_libros autlib 
    INNER JOIN libros lib ON autlib.Libros_idLibro = lib.idLibro 
    INNER JOIN autores aut ON autlib.Autores_idAutor = aut.idAutor
     */
    public ControladorAsociarAutor(Autor modeloAutor, Libro modeloLibro, AsociarAutor vista, AsociarAutorSql sql) {
        this.modeloAutor = modeloAutor;
        this.modeloLibro = modeloLibro;
        this.vista = vista;
        this.sql = sql;
        this.vista.Submenu_salir.addActionListener(this);
        this.vista.Submenu_volver.addActionListener(this);

        this.vista.btn_asociar.addActionListener(this);
        this.vista.btn_quitar.addActionListener(this);

        this.vista.btn_asociar.setEnabled(false);
        this.vista.btn_quitar.setEnabled(false);

        muestrate();
    }

    public void muestrate() {
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión asociar autor");
        this.vista.txt_tituloTabla.setText("Autores asociados con libbro: " + this.modeloLibro.getNombre());

        llenar(this.vista.tabla, true);
        llenar(this.vista.tablaAuotesAñadidos, false);

        modeloTablaRescatado = (DefaultTableModel) vista.tabla.getModel();

        seleccionarTabla(this.vista.tabla, true);
        seleccionarTabla(this.vista.tablaAuotesAñadidos, false);

//        buscarEnTabla(this.vista.campo_busqueda, this.vista.tabla, this.vista.btn_asociar);
//        buscarEnTabla(this.vista.campo_buscartabla, this.vista.tablaAuotesAñadidos, this.vista.btn_quitar);

        this.vista.campo_numSerie.setEditable(false);
        this.vista.campo_nombreLibro.setEditable(false);
        this.vista.campo_numSerie.setText(String.valueOf(modeloLibro.getIsbn()));
        this.vista.campo_nombreLibro.setText(String.valueOf(modeloLibro.getNombre()));

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(this.vista.Submenu_volver)) {
            volver();
        }

        if (ae.getSource().equals(this.vista.Submenu_salir)) {
            SALIR(this.vista);
        }

        if (ae.getSource().equals(this.vista.btn_asociar)) {
            asociar();
        }

        if (ae.getSource().equals(this.vista.btn_quitar)) {
            quitar();
        }
    }

    private boolean llenar(JTable tabla, boolean igualdad) {
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

        int id = modeloLibro.getId();
        int listadoid[] = sql.idAutoresSinAñadir(id, sql.getCantidad(id));

        tabla.setModel(modeloTabla);
        tabla.getTableHeader().setReorderingAllowed(false);

        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellido Paterno");
        modeloTabla.addColumn("Apellido Materno");

        ArrayList<Autor> listadoAutores = sql.listadoAutoresAñadir(listadoid, igualdad, id);

        Object[] colum = new Object[3];
        for (int i = 0; i < listadoAutores.size(); i++) {
            colum[0] = listadoAutores.get(i).getNombre();
            colum[1] = listadoAutores.get(i).getApellido_p();
            colum[2] = listadoAutores.get(i).getApellido_m();

            modeloTabla.addRow(colum);
        }

        return true;
    }

    private void seleccionarTabla(JTable tb, boolean cual) {
        try {
            //si cual es tru hace la tabla asociar
            //si cual es false hace la tabla quitar
            tb.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    autTemp = null;
                    int fila = tb.getSelectedRow();
                    System.out.println(fila);

                    String nombre = tb.getValueAt(fila, 0).toString();
                    String ape_p = tb.getValueAt(fila, 1).toString();
                    String ape_m = tb.getValueAt(fila, 2).toString();

                    modeloAutor.setNombre(nombre);
                    modeloAutor.setApellido_p(ape_p);
                    modeloAutor.setApellido_m(ape_m);

                    String datoTabla = nombre + "" + ape_p + "" + ape_m;

                    if (sql.Verificar(modeloAutor).equals(datoTabla)) {

                        String datoSQL = modeloAutor.getNombre() + " " + modeloAutor.getApellido_p() + " " + modeloAutor.getApellido_m();
                        System.out.println("ID AUTOR: " + modeloAutor.getId());

                        if (cual) {
                            vista.btn_asociar.setEnabled(true);
                        } else {
                            vista.btn_quitar.setEnabled(true);
                        }
                        autTemp = modeloAutor;
                        System.out.println("_____________________________________");
                        System.out.println(autTemp.toString());
                    }

                }
            });
        } catch (Exception e) {
            System.err.println("seleccionar tabla: " + e);
        }
    }

    private void volver() {
        int resp = JOptionPane.showConfirmDialog(null, "¿Realmente deseas volver?");
        if (resp == JOptionPane.YES_OPTION) {
            Libro est = new Libro();
            LibroSql estSql = new LibroSql();
            GestionarLibro viewEst = new GestionarLibro();

            ControladorLibro ctrl = new ControladorLibro(est, estSql, viewEst);

            this.vista.dispose();
        }
    }

    private void asociar() {
        try {
            if (!autTemp.equals(null)) {
                if (sql.insert(modeloLibro.getId(), autTemp.getId())) {
                    JOptionPane.showMessageDialog(null, "Asociado");

                    llenar(this.vista.tabla, true);
                    llenar(this.vista.tablaAuotesAñadidos, false);

                    this.vista.btn_asociar.setEnabled(false);
                } else {

                    JOptionPane.showMessageDialog(null, "Error");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un autor de la tabla");
            }
        } catch (Exception e) {
            System.err.println("asociar xdd: " + e);
        }
    }

    private void quitar() {
        try {
            if (!autTemp.equals(null)) {
                if (sql.delete(modeloLibro.getId(), autTemp.getId())) {
                    JOptionPane.showMessageDialog(null, "Asociación eliminado");
                    llenar(this.vista.tabla, true);
                    llenar(this.vista.tablaAuotesAñadidos, false);

                    this.vista.btn_quitar.setEnabled(false);
                    autTemp = null;
                } else {

                    JOptionPane.showMessageDialog(null, "Error");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un autor de la tabla");
            }
        } catch (Exception e) {
            System.err.println("asociar: " + e);
        }
    }

}

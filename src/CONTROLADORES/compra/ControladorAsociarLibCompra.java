/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.compra;

import CONTROLADORES.ventas.ControladorVentas;
import static MODELOS.Helpers.SALIR;
import MODELOS.MODELOCLASES.compras.Compras;
import MODELOS.MODELOCLASES.libros.Libro; 
import MODELOS.SQL.compras.AsociarCompraLibroSql; 
import MODELOS.SQL.compras.ComprasSql;
import MODELOS.SQL.ventas.VentasSql;
import VISTAS.compras.GestionarCompras;
import VISTAS.ventas.GestionarAsocLibVentas;
import VISTAS.ventas.GestionarVentas;
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
public class ControladorAsociarLibCompra implements ActionListener{
    private Libro modelLibro;
    private Compras modelCompra;
    private AsociarCompraLibroSql sql;
    private GestionarAsocLibVentas vista;

    private Libro libTemp = null;

    public ControladorAsociarLibCompra(Libro modelLibro, Compras modelCompra, AsociarCompraLibroSql sql, GestionarAsocLibVentas vista) {
        this.modelLibro = modelLibro;
        this.modelCompra = modelCompra;
        this.sql = sql;
        this.vista = vista;
        this.vista.Submenu_salir.addActionListener(this);
        this.vista.Submenu_volver.addActionListener(this);
        System.out.println("[MODELO DE VENTAS -> "+this.modelCompra.toString()+"]");

        this.vista.btn_asociar.addActionListener(this);
        this.vista.btn_quitar.addActionListener(this);

        this.vista.btn_asociar.setEnabled(false);
        this.vista.btn_quitar.setEnabled(false);
    
        muestrate();
    }

    public void muestrate() {
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión añadir libros a compra");
        this.vista.txt_tituloTabla.setText("Añadir libros a compra: " + this.modelCompra.getCodigo_compra());

        llenar(this.vista.tabla, true);
        llenar(this.vista.tablaAgregados, false);
 

        seleccionarTabla(this.vista.tabla, true);
        seleccionarTabla(this.vista.tablaAgregados, false);
 
        this.vista.campo_numSerie.setEditable(false); 
        this.vista.campo_numSerie.setText(String.valueOf(modelCompra.getCodigo_compra()));
    }
    
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(this.vista.Submenu_volver)) {
            VOLVER();
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

        int id = modelCompra.getId();
        int listadoid[] = sql.idLibrosSinAñadir(id, sql.getCantidad(id));

        tabla.setModel(modeloTabla);
        tabla.getTableHeader().setReorderingAllowed(false);

        modeloTabla.addColumn("#");
        modeloTabla.addColumn("ISBN");
        modeloTabla.addColumn("N° de serie");
        modeloTabla.addColumn("Nombre libro"); 

        ArrayList<Libro> listado = sql.milistadoLibros(listadoid, igualdad, id);

        Object[] colum = new Object[4];
        for (int i = 0; i < listado.size(); i++) {
            //lib.idLibro, lib.isbn, lib.num_serie, lib.nombre
            colum[0] = listado.get(i).getId();
            colum[1] = listado.get(i).getIsbn();
            colum[2] = listado.get(i).getNumSerie();
            colum[3] = listado.get(i).getNombre();  
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
                    libTemp = null;
                    int fila = tb.getSelectedRow();
                    System.out.println(fila); 
                    int id = Integer.parseInt(tb.getValueAt(fila, 0).toString());   
                    modelLibro.setId(id);  
                    if (sql.VerificarLib(modelLibro)) { 
                        System.out.println("Dato sql -> " +modelLibro.mostrarDatos()); 
                        if (cual) {
                            vista.btn_asociar.setEnabled(true);
                        } else {
                            vista.btn_quitar.setEnabled(true);
                        }
                        libTemp = modelLibro;
                        System.out.println("_____________________________________");
                        System.out.println("Correo TEMP -> "+ libTemp.toString());
                    } 
                }
            });
        } catch (Exception e) {
            System.err.println("seleccionar tabla: " + e);
        }
    }
    private void VOLVER() {
        int resp = JOptionPane.showConfirmDialog(null, "¿Realmente deseas volver?");
        if (resp == JOptionPane.YES_OPTION) {
            Compras est = new Compras();
            ComprasSql estSql = new ComprasSql();
            GestionarCompras viewEst = new GestionarCompras();

            ControladorCompra ctrl = new ControladorCompra(est, estSql, viewEst);

            this.vista.dispose();
        }
    } 
    
    
    private void asociar() {
        try {
            if (!libTemp.equals(null)) {
                if (sql.insert(modelCompra.getId(), libTemp.getId())) {
                    JOptionPane.showMessageDialog(null, "Asociado");

                    llenar(this.vista.tabla, true);
                    llenar(this.vista.tablaAgregados, false);

                    this.vista.btn_asociar.setEnabled(false);
                } else {

                    JOptionPane.showMessageDialog(null, "Error");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla");
            }
        } catch (Exception e) {
            System.err.println("asociar xdd: " + e);
        }
    }

    private void quitar() {
        try {
            if (!libTemp.equals(null)) {
                if (sql.delete(modelCompra.getId(), libTemp.getId())) {
                    JOptionPane.showMessageDialog(null, "Asociación eliminado");
                    llenar(this.vista.tabla, true);
                    llenar(this.vista.tablaAgregados, false);

                    this.vista.btn_quitar.setEnabled(false);
                    libTemp = null;
                } else {

                    JOptionPane.showMessageDialog(null, "Error");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla");
            }
        } catch (Exception e) {
            System.err.println("asociar: " + e);
        }
    }
}

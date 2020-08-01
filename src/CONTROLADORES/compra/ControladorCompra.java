/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.compra;

import MODELOS.Helpers;
import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER;
import MODELOS.MODELOCLASES.compras.Compras;
import MODELOS.MODELOCLASES.compras.Facturas;
import MODELOS.MODELOCLASES.libros.Editorial;
import MODELOS.MODELOCLASES.libros.Estado;
import MODELOS.MODELOCLASES.libros.Libro;
import MODELOS.MODELOCLASES.personas.Distribuidor;
import MODELOS.SQL.compras.AsociarCompraLibroSql;
import MODELOS.SQL.compras.ComprasSql;
import VISTAS.compras.GestionarCompras;
import VISTAS.ventas.GestionarAsocLibVentas;
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
public class ControladorCompra implements ActionListener {

    private Compras modelo;
    private ComprasSql sql;
    private GestionarCompras vista;

    public ControladorCompra(Compras modelo, ComprasSql sql, GestionarCompras vista) {
        this.modelo = modelo;
        this.sql = sql;
        this.vista = vista;
        this.vista.Submenu_salir.addActionListener(this);
        this.vista.Submenu_volver.addActionListener(this);
        this.vista.btn_add.addActionListener(this);
        this.vista.btn_remove.addActionListener(this);
        this.vista.btn_update.addActionListener(this);
        this.vista.btn_clear.addActionListener(this);
        this.vista.btn_generarCodigo.addActionListener(this);
        this.vista.btn_seleccionarLibros.addActionListener(this);


        muestrate();
    }

    public void muestrate() {
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de facturas");

        llenar(this.vista.Tabla);
        seleccionarTabla(this.vista.Tabla);
        Helpers.cargarComboDistribudiores(this.vista.combo_distribuidores);
        Helpers.cargarComboFactura(this.vista.combo_facturas);
//
//        buscarEnTabla(this.vista.campo_buscar, this.vista.Tabla_estados);
//        Helpers.soloLetras(this.vista.campo_cateogira);
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

        if (ae.getSource().equals(this.vista.btn_seleccionarLibros)) {
            seleccionarLibros();
        }

        if (ae.getSource().equals(this.vista.btn_generarCodigo)) {
            this.vista.campo_codigo.setText(String.valueOf(Helpers.numAleatorio()));
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
        modeloTabla.addColumn("Cod. Compra");
        modeloTabla.addColumn("Rut distribuidor");
        modeloTabla.addColumn("Nombre empresa");
        modeloTabla.addColumn("Folio factura");
        modeloTabla.addColumn("Prefico neto");
        modeloTabla.addColumn("Fecha compra"); 

        Object[] colum = new Object[7];
        //idCompra	codigoCompra	Facturas_idFactura	
        //Distribuidores_idDistribuidore	idDistribuidore	rut	
        //nombre_empresa	anno_sociedad	Telefonos_idTelefono	
        //Direcciones_idDireccion	
        //idFactura	folio	precio_neto	
        //precio_iva	costo_iva	fecha_compra	MetodosPagos_idMetodoPago
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getId();
            colum[1] = sql.listar().get(i).getCodigo_compra();
            colum[2] = sql.listar().get(i).getDistribuidor().getRut();
            colum[3] = sql.listar().get(i).getDistribuidor().getNombre_empresa();
            colum[4] = sql.listar().get(i).getFactura().getFolio();
            colum[5] = sql.listar().get(i).getFactura().getPrecio_neto();
            colum[6] = sql.listar().get(i).getFactura().getFecha_compra(); 

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
                    vista.campo_codigo.setText(modelo.getCodigo_compra()); 

                    ComboBoxModel modeloCombo = vista.combo_distribuidores.getModel();
                    for (int i = 0; i < modeloCombo.getSize(); i++) {
                        if (modeloCombo.getElementAt(i).toString().equals(modelo.getDistribuidor().toString())) {
                            vista.combo_distribuidores.setSelectedIndex(i);
                            break;
                        }
                    }

                    ComboBoxModel modeloCombov = vista.combo_facturas.getModel();
                    for (int i = 0; i < modeloCombov.getSize(); i++) {
                        if (modeloCombov.getElementAt(i).toString().equals(modelo.getFactura().toString())) {
                            vista.combo_facturas.setSelectedIndex(i);
                            break;
                        }
                    }

                    System.out.println(modelo.toString());

                }
            }
        });
    }

    private void add() {
        try {
            if (validaCampos()) {

                this.modelo.setId(modelo.getId());
                this.modelo.setCodigo_compra(this.vista.campo_codigo.getText());
                this.modelo.setDistribuidor((Distribuidor) vista.combo_distribuidores.getSelectedItem());
                this.modelo.setFactura((Facturas) vista.combo_facturas.getSelectedItem());

                System.out.println(this.modelo.toString());

                if (sql.VerificarCodigo(modelo) == 0) {
                    if (sql.INSERT(modelo)) {
                        JOptionPane.showMessageDialog(null, "Compra registrado.");
                        llenar(this.vista.Tabla);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ya se ha registrado la compra");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un elemento de la tabla");
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
                        JOptionPane.showMessageDialog(null, "Compra eliminado");
                        llenar(this.vista.Tabla);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "error");
                    }

                }

            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un elemento de la tabla");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Procure NO ingresar letras y dejarlos vacios en campos dentinados para numeros:");
            System.err.println(e);
        }
    }

    private void update() {
        try {
            if (validaCampos()) {

                this.modelo.setId(modelo.getId());
                this.modelo.setCodigo_compra(this.vista.campo_codigo.getText());
                this.modelo.setDistribuidor((Distribuidor) vista.combo_distribuidores.getSelectedItem());
                this.modelo.setFactura((Facturas) vista.combo_facturas.getSelectedItem());

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
        if(this.vista.campo_codigo.getText().isEmpty()){
            est = false;
        }
        if(this.vista.combo_distribuidores.getSelectedIndex() == 0){
            est = false;
        }
        if(this.vista.combo_facturas.getSelectedIndex() == 0){
            est = false;
        }
        return est;
    }

    private void limpiar() {
        this.vista.campo_codigo.setText(null);
        this.vista.combo_distribuidores.setSelectedIndex(0);
        this.vista.combo_facturas.setSelectedIndex(0);
    }

    private void seleccionarLibros() {
        try {
            
            if(validaCampos()){ 
                //el modelo ya esta lleno 
                Libro au = new Libro();
                AsociarCompraLibroSql asoc = new AsociarCompraLibroSql();
                GestionarAsocLibVentas ausql = new GestionarAsocLibVentas();
                ControladorAsociarLibCompra control = new ControladorAsociarLibCompra(au, modelo, asoc, ausql);
                this.vista.dispose();
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione un libro de la tabla");
            }
            
        } catch (Exception e) {
            System.err.println("asociarAutor: " + e);
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.arriendos;

import MODELOS.Helpers;
import static MODELOS.Helpers.SALIR;
import static MODELOS.Helpers.VOLVER;
import MODELOS.MODELOCLASES.arriendos.Arriendos;
import MODELOS.MODELOCLASES.libros.Editorial;
import MODELOS.MODELOCLASES.libros.Estado;
import MODELOS.MODELOCLASES.libros.Libro;
import MODELOS.MODELOCLASES.personas.Cliente;
import MODELOS.MODELOCLASES.personas.Trabajador;
import MODELOS.SQL.arriendos.ArriendosSql;
import MODELOS.SQL.arriendos.AsociarArriendoLibroSql;
import VISTAS.arriendos.GestionarArriendos;
import VISTAS.ventas.GestionarAsocLibVentas;
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
public class ControladorArriendos implements ActionListener {

    private Arriendos modelo;
    private ArriendosSql sql;
    private GestionarArriendos vista;

    public ControladorArriendos(Arriendos modelo, ArriendosSql sql, GestionarArriendos vista) {
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
        this.vista.btn_asociarLibros.addActionListener(this);

        muestrate();
    }

    public void muestrate() {
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión de arriendos");

        llenar(this.vista.Tabla_estados);
        seleccionarTabla(this.vista.Tabla_estados);
        this.vista.campo_codigo.setEditable(false);
        Helpers.cargarComboTrabajadores(this.vista.combo_trabajadores);
        Helpers.cargarComboClientes(this.vista.como_clientes);
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

        if (ae.getSource().equals(this.vista.btn_generarCodigo)) {
            this.vista.campo_codigo.setText(String.valueOf(Helpers.numAleatorio()));
        }

        if (ae.getSource().equals(this.vista.btn_clear)) {
            limpiar();
        }

        if (ae.getSource().equals(this.vista.btn_asociarLibros)) {
            asociarLibros();
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
        modeloTabla.addColumn("Codigo");
        modeloTabla.addColumn("Cost. Total");
        modeloTabla.addColumn("Cost. Arriendo");
        modeloTabla.addColumn("Fecha Arriendo");
        modeloTabla.addColumn("Fecha Devolución");
        modeloTabla.addColumn("Fecha Entrega");
        modeloTabla.addColumn("Dias atrazo");
        modeloTabla.addColumn("Multa");
        modeloTabla.addColumn("Rut Cliente");
        modeloTabla.addColumn("Rut Trabjador");

        Object[] colum = new Object[11];
        //IDARRI	CODARRI	COSTALARRI	COSTARRI	FECHARRI	FECHDEVOLARRI	
        //FECHENTREARRI	DIASRETRAARRI	MULTARRI	IDCLI	RUTCLI	NOMCLI	APEPCLI	APEMCLI	
        //IDTRA	RUTTRA	NOMTRA	APEPTRA	APEMTRA
        for (int i = 0; i < sql.listar().size(); i++) {
            colum[0] = sql.listar().get(i).getId();
            colum[1] = sql.listar().get(i).getCodigo();
            colum[2] = sql.listar().get(i).getCosto_total();
            colum[3] = sql.listar().get(i).getCosto_arriendo();
            colum[4] = sql.listar().get(i).getFecha_arriendo();
            colum[5] = sql.listar().get(i).getFecha_devolucion();
            colum[6] = sql.listar().get(i).getFecha_entrega();
            colum[7] = sql.listar().get(i).getDias_atrazo();
            colum[8] = sql.listar().get(i).getMulta();
            colum[9] = sql.listar().get(i).getCliente().getRut();
            colum[10] = sql.listar().get(i).getTrabjador().getRut();
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
                //IDARRI	CODARRI	COSTALARRI	COSTARRI	FECHARRI	FECHDEVOLARRI	
                //FECHENTREARRI	DIASRETRAARRI	MULTARRI	IDCLI	RUTCLI	NOMCLI	APEPCLI	APEMCLI	
                //IDTRA	RUTTRA	NOMTRA	APEPTRA	APEMTRA
                if (sql.SELECT(modelo)) {
                    vista.campo_codigo.setText(modelo.getCodigo());
                    vista.campo_costoTotal.setText(String.valueOf(modelo.getCosto_total()));
                    vista.campo_Costoarriendo.setText(String.valueOf(modelo.getCosto_arriendo()));
                    vista.fecha_arriendo.setDate(modelo.getFecha_arriendo());
                    vista.fecha_devolucion.setDate(modelo.getFecha_devolucion());
                    vista.fecha_entrega.setDate(modelo.getFecha_entrega());
                    vista.campo_diasAtrazo.setText(String.valueOf(modelo.getDias_atrazo()));
                    vista.campo_multa.setText(String.valueOf(modelo.getMulta()));

                    ComboBoxModel modeloCombo = vista.combo_trabajadores.getModel();
                    for (int i = 0; i < modeloCombo.getSize(); i++) {
                        if (modeloCombo.getElementAt(i).toString().equals(modelo.getTrabjador().toString())) {
                            vista.combo_trabajadores.setSelectedIndex(i);
                            break;
                        }
                    }

                    ComboBoxModel modeloCombov = vista.como_clientes.getModel();
                    for (int i = 0; i < modeloCombov.getSize(); i++) {
                        if (modeloCombov.getElementAt(i).toString().equals(modelo.getCliente().toString())) {
                            vista.como_clientes.setSelectedIndex(i);
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

                modelo.setId(modelo.getId());
                modelo.setCodigo(vista.campo_codigo.getText());
                modelo.setCosto_total(Float.parseFloat(vista.campo_costoTotal.getText()));
                modelo.setCosto_arriendo(Float.parseFloat(vista.campo_Costoarriendo.getText()));

                Date date = (Date) vista.fecha_arriendo.getDate();
                long d = date.getTime();
                java.sql.Date fechaArriendo = new java.sql.Date(d);

                modelo.setFecha_arriendo(fechaArriendo);

                Date dateD = (Date) vista.fecha_devolucion.getDate();
                long dD = date.getTime();
                java.sql.Date fechaDevol = new java.sql.Date(d);

                modelo.setFecha_devolucion(fechaDevol);

                Date dateA = (Date) vista.fecha_entrega.getDate();
                long dA = date.getTime();
                java.sql.Date fechA = new java.sql.Date(d);

                modelo.setFecha_entrega(fechA);
                modelo.setDias_atrazo(Integer.parseInt(vista.campo_diasAtrazo.getText()));
                modelo.setMulta(Float.parseFloat(vista.campo_multa.getText()));
                modelo.setCliente((Cliente) this.vista.como_clientes.getSelectedItem());
                modelo.setTrabjador((Trabajador) this.vista.combo_trabajadores.getSelectedItem());

                System.out.println(this.modelo.toString());

                if (sql.Verificar(modelo) == 0) {
                    if (sql.INSERT(modelo)) {
                        JOptionPane.showMessageDialog(null, "Libro registrado.");
                        llenar(this.vista.Tabla_estados);
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
        }
    }

    private boolean validaCampos() {
        boolean est = true;
        if (this.vista.campo_Costoarriendo.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.campo_codigo.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.campo_costoTotal.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.campo_diasAtrazo.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.campo_multa.getText().isEmpty()) {
            est = false;
        }
        if (this.vista.combo_trabajadores.getSelectedIndex() == 0) {
            est = false;
        }
        if (this.vista.como_clientes.getSelectedIndex() == 0) {
            est = false;
        }
        return est;
    }

    private void limpiar() {
        this.vista.campo_Costoarriendo.setText(null);
        this.vista.campo_codigo.setText(null);
        this.vista.campo_costoTotal.setText(null);
        this.vista.campo_diasAtrazo.setText(null);
        this.vista.campo_multa.setText(null);
        this.vista.combo_trabajadores.setSelectedIndex(0);
        this.vista.como_clientes.setSelectedIndex(0);
        this.vista.fecha_arriendo.setDate(null);
        this.vista.fecha_devolucion.setDate(null);
        this.vista.fecha_entrega.setDate(null);
    }

    private void remove() {
        try {
            if (validaCampos()) {  
                int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este registro?");
                if (opSelect == JOptionPane.YES_OPTION) { 
                    if (sql.REMOVE(modelo)) {
                        JOptionPane.showMessageDialog(null, "Arriendo eliminado");
                        llenar(this.vista.Tabla_estados);
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
                modelo.setId(modelo.getId());
                modelo.setCodigo(vista.campo_codigo.getText());
                modelo.setCosto_total(Float.parseFloat(vista.campo_costoTotal.getText()));
                modelo.setCosto_arriendo(Float.parseFloat(vista.campo_Costoarriendo.getText()));

                Date date = (Date) vista.fecha_arriendo.getDate();
                long d = date.getTime();
                java.sql.Date fechaArriendo = new java.sql.Date(d);

                modelo.setFecha_arriendo(fechaArriendo);

                Date dateD = (Date) vista.fecha_devolucion.getDate();
                long dD = date.getTime();
                java.sql.Date fechaDevol = new java.sql.Date(d);

                modelo.setFecha_devolucion(fechaDevol);

                Date dateA = (Date) vista.fecha_entrega.getDate();
                long dA = date.getTime();
                java.sql.Date fechA = new java.sql.Date(d);

                modelo.setFecha_entrega(fechA);
                modelo.setDias_atrazo(Integer.parseInt(vista.campo_diasAtrazo.getText()));
                modelo.setMulta(Float.parseFloat(vista.campo_multa.getText()));
                modelo.setCliente((Cliente) this.vista.como_clientes.getSelectedItem());
                modelo.setTrabjador((Trabajador) this.vista.combo_trabajadores.getSelectedItem());

                int opSelect = JOptionPane.showConfirmDialog(null, "¿Desea actualizar este registro?");
                if (opSelect == JOptionPane.YES_OPTION) {

                    if (sql.UPDATE(modelo)) {
                        JOptionPane.showMessageDialog(null, "Arriendo actualizado");
                        llenar(this.vista.Tabla_estados);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error");
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

    private void asociarLibros() {
        try {
            
            if(validaCampos()){ 
                //el modelo ya esta lleno 
                Libro au = new Libro();
                GestionarAsocLibVentas asoc = new GestionarAsocLibVentas();
                AsociarArriendoLibroSql ausql = new AsociarArriendoLibroSql();
                ControladorAsociarLibrosArriendo control = new ControladorAsociarLibrosArriendo(au, modelo, ausql, asoc);
                this.vista.dispose();
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione un libro de la tabla");
            }
            
        } catch (Exception e) {
            System.err.println("asociarAutor: " + e);
        }
    }

}

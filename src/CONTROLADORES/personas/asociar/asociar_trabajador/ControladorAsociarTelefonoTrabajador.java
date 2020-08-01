/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES.personas.asociar.asociar_trabajador;

import CONTROLADORES.personas.ControladorTrabajador;
import static MODELOS.Helpers.SALIR;
import MODELOS.MODELOCLASES.personas.Trabajador;
import MODELOS.MODELOCLASES.personas.datos.Correo;
import MODELOS.MODELOCLASES.personas.datos.Telefono;
import MODELOS.SQL.personas.TrabajadorSql;
import MODELOS.SQL.personas.asociar.AsociarNumeroSql;
import VISTAS.personas.AsociarNumero;
import VISTAS.personas.GestionarTrabajadores;
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
public class ControladorAsociarTelefonoTrabajador implements ActionListener{

    private Trabajador modeloTra;
    private Telefono modelTel;
    private AsociarNumero vista;
    private AsociarNumeroSql sql;
    
    private Telefono telTemp = null;
    private DefaultTableModel modeloTablaRescatado = null;
    
    public ControladorAsociarTelefonoTrabajador(Trabajador modeloTra, Telefono modelTel, AsociarNumero vista, AsociarNumeroSql sql) {
        this.modeloTra = modeloTra;
        this.modelTel = modelTel;
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
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión asociar telefono trabajador");
        this.vista.txt_tituloTabla.setText("Telefonos asociados con Trabajador: " + this.modeloTra.getNombre());

        llenar(this.vista.tabla, true);
        llenar(this.vista.tablaAuotesAñadidos, false);

        modeloTablaRescatado = (DefaultTableModel) vista.tabla.getModel();

        seleccionarTabla(this.vista.tabla, true);
        seleccionarTabla(this.vista.tablaAuotesAñadidos, false);
 
        this.vista.campo_nombre.setEditable(false);
        this.vista.campo_rut.setEditable(false);
        this.vista.campo_rut.setText(String.valueOf(modeloTra.getRut()));
        this.vista.campo_nombre.setText(modeloTra.getNombre()+" "+modeloTra.getApellido_p()+" "+modeloTra.getApellido_m());

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

    //PENDIENTE LAS ASOCUACIONES ENTRE ESTOS REGISTROS
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

        int id = modeloTra.getId();
        int listadoid[] = sql.idTelefonoSinAñadir(id, sql.getCantidad(id));

        tabla.setModel(modeloTabla);
        tabla.getTableHeader().setReorderingAllowed(false);

        modeloTabla.addColumn("#");
        modeloTabla.addColumn("Telefono");

        ArrayList<Telefono> listado = sql.listadoTelefonoAñadir(listadoid, igualdad, id);

        Object[] colum = new Object[2];
        for (int i = 0; i < listado.size(); i++) {
            colum[0] = listado.get(i).getId();
            colum[1] = listado.get(i).getNumero(); 

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
                    telTemp = null;
                    int fila = tb.getSelectedRow();
                    System.out.println(fila);

                    int id = Integer.parseInt(tb.getValueAt(fila, 0).toString()); 
                    String tel = tb.getValueAt(fila, 1).toString();  

                    modelTel.setId(id);
                    modelTel.setNumero(tel); 

                    if (sql.VerificarTel(modelTel)) {

                        System.out.println("Dato sql -> " +modelTel.toString());

                        if (cual) {
                            vista.btn_asociar.setEnabled(true);
                        } else {
                            vista.btn_quitar.setEnabled(true);
                        }
                        telTemp = modelTel;
                        System.out.println("_____________________________________");
                        System.out.println("Correo TEMP -> "+ telTemp.toString());
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
            Trabajador est = new Trabajador();
            TrabajadorSql estSql = new TrabajadorSql();
            GestionarTrabajadores viewEst = new GestionarTrabajadores();

            ControladorTrabajador ctrl = new ControladorTrabajador(est, estSql, viewEst);

            this.vista.dispose();
        }
    } 
    

    private void asociar() {
        try {
            if (!telTemp.equals(null)) {
                if (sql.insert(modeloTra.getId(), telTemp.getId())) {
                    JOptionPane.showMessageDialog(null, "Asociado");

                    llenar(this.vista.tabla, true);
                    llenar(this.vista.tablaAuotesAñadidos, false);

                    this.vista.btn_asociar.setEnabled(false);
                } else {

                    JOptionPane.showMessageDialog(null, "Error");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un correo de la tabla");
            }
        } catch (Exception e) {
            System.err.println("asociar xdd: " + e);
        }
    }

    private void quitar() {
        try {
            if (!telTemp.equals(null)) {
                if (sql.delete(modeloTra.getId(), telTemp.getId())) {
                    JOptionPane.showMessageDialog(null, "Asociación eliminado");
                    llenar(this.vista.tabla, true);
                    llenar(this.vista.tablaAuotesAñadidos, false);

                    this.vista.btn_quitar.setEnabled(false);
                    telTemp = null;
                } else {

                    JOptionPane.showMessageDialog(null, "Error");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un correo de la tabla");
            }
        } catch (Exception e) {
            System.err.println("asociar: " + e);
        }
    }
    
}

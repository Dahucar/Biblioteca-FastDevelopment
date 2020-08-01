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
import MODELOS.SQL.personas.TrabajadorSql;
import MODELOS.SQL.personas.asociar.AsociarCorreoSql;
import VISTAS.personas.AsociarCorreos;
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
public class ControladorAsociarCorreoTrabajador implements ActionListener{
    private Trabajador modeloTra;
    private Correo modelCorreo;
    private AsociarCorreos vista;
    private AsociarCorreoSql sql;
    
    
    private Correo corrTemp = null;
    private DefaultTableModel modeloTablaRescatado = null;

    public ControladorAsociarCorreoTrabajador(Trabajador modeloTra, Correo modelCorreo, AsociarCorreos vista, AsociarCorreoSql sql) {
        this.modeloTra = modeloTra;
        this.modelCorreo = modelCorreo;
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
        this.vista.setTitle("Biblioteca Fastdevelopment | Gestión asociar correo trabajador");
        this.vista.txt_tituloTabla.setText("Correos asociados con trabajador: " + this.modeloTra.getNombre());

        llenar(this.vista.tabla, true);
        llenar(this.vista.tablaAgregados, false);

        modeloTablaRescatado = (DefaultTableModel) vista.tabla.getModel();

        seleccionarTabla(this.vista.tabla, true);
        seleccionarTabla(this.vista.tablaAgregados, false);
 
        this.vista.campo_rut.setEditable(false);
        this.vista.campo_nombre.setEditable(false);
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
        int listadoid[] = sql.idCorreosSinAñadir(id, sql.getCantidad(id));

        tabla.setModel(modeloTabla);
        tabla.getTableHeader().setReorderingAllowed(false);

        modeloTabla.addColumn("#");
        modeloTabla.addColumn("Correo");
        modeloTabla.addColumn("Contraseña");

        ArrayList<Correo> listado = sql.listadoCorreosAñadir(listadoid, igualdad, id);

        Object[] colum = new Object[3];
        for (int i = 0; i < listado.size(); i++) {
            colum[0] = listado.get(i).getId();
            colum[1] = listado.get(i).getCorreo();
            colum[2] = listado.get(i).getContrasenna();

            modeloTabla.addRow(colum);
        }

        return true;
    }

    private void asociar() {
        try {
            if (!corrTemp.equals(null)) {
                if (sql.insert(modeloTra.getId(), corrTemp.getId())) {
                    JOptionPane.showMessageDialog(null, "Asociado");

                    llenar(this.vista.tabla, true);
                    llenar(this.vista.tablaAgregados, false);

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
            if (!corrTemp.equals(null)) {
                if (sql.delete(modeloTra.getId(), corrTemp.getId())) {
                    JOptionPane.showMessageDialog(null, "Asociación eliminado");
                    llenar(this.vista.tabla, true);
                    llenar(this.vista.tablaAgregados, false);

                    this.vista.btn_quitar.setEnabled(false);
                    corrTemp = null;
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
    
    private void seleccionarTabla(JTable tb, boolean cual) {
        try {
            //si cual es tru hace la tabla asociar
            //si cual es false hace la tabla quitar
            tb.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    corrTemp = null;
                    int fila = tb.getSelectedRow();
                    System.out.println(fila);

                    int idCorr = Integer.parseInt(tb.getValueAt(fila, 0).toString()); 
                    String corrCorr = tb.getValueAt(fila, 1).toString(); 
                    String passCorr = tb.getValueAt(fila, 2).toString(); 

                    modelCorreo.setId(idCorr);
                    modelCorreo.setCorreo(corrCorr);
                    modelCorreo.setContrasenna(passCorr);

                    if (sql.VerificarCorreo(modelCorreo)) {

                        System.out.println("Dato sql -> " +modelCorreo.toString());

                        if (cual) {
                            vista.btn_asociar.setEnabled(true);
                        } else {
                            vista.btn_quitar.setEnabled(true);
                        }
                        corrTemp = modelCorreo;
                        System.out.println("_____________________________________");
                        System.out.println("Correo TEMP -> "+ corrTemp.toString());
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
    
}

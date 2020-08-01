/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS;

import CONTROLADORES.ControladorPrincipal;
import MODELOS.MODELOCLASES.compras.Facturas;
import MODELOS.MODELOCLASES.libros.Editorial;
import MODELOS.MODELOCLASES.libros.Estado;
import MODELOS.MODELOCLASES.libros.Libro;
import MODELOS.MODELOCLASES.personas.Cliente;
import MODELOS.MODELOCLASES.personas.Distribuidor;
import MODELOS.MODELOCLASES.personas.Trabajador;
import MODELOS.MODELOCLASES.personas.datos.Direccion;
import MODELOS.MODELOCLASES.personas.datos.Telefono;
import MODELOS.MODELOCLASES.ventas.Boletas;
import MODELOS.MODELOCLASES.ventas.MetodoPago;
import MODELOS.SQL.compras.FacturasSql;
import MODELOS.SQL.libro.EditorialSql;
import MODELOS.SQL.libro.EstadoSql;
import MODELOS.SQL.libro.LibroSql;
import MODELOS.SQL.personas.ClienteSql;
import MODELOS.SQL.personas.DistribuidorSql;
import MODELOS.SQL.personas.TrabajadorSql;
import MODELOS.SQL.personas.datos.DireccionSql;
import MODELOS.SQL.personas.datos.TelefonoSql;
import MODELOS.SQL.ventas.BoletaSql;
import MODELOS.SQL.ventas.MetodoPagoSql;
import VISTAS.MenuInicio;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Daniel Huenul
 */
public class Helpers {

    public static int numAleatorio() {
        int valorEntero = (int) Math.floor(Math.random() * (99999999 - 15623548 + 413311) + 3123);
        return valorEntero;
    }

    public static void VOLVER(JFrame vista) {
        int resp = JOptionPane.showConfirmDialog(null, "¿Realmente deseas volver?");
        if (resp == JOptionPane.YES_OPTION) {
            MenuInicio view = new MenuInicio();
            ControladorPrincipal ctrl = new ControladorPrincipal(view);
            vista.dispose();
        }
    }

    public static void SALIR(JFrame vista) {
        int resp = JOptionPane.showConfirmDialog(null, "¿Realmente deseas salir del programa?");
        if (resp == JOptionPane.YES_OPTION) {
            vista.dispose();
        }
    }

    public static void soloNumeros(JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("keyReleased");
            }

            @Override
            public void keyTyped(KeyEvent e) {
                char validar = e.getKeyChar();

                if (Character.isLetter(validar)) {

                    e.consume();
                    JOptionPane.showMessageDialog(null, "Solo debe ingresar números");

                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
    }

    public static void soloLetras(JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("keyReleased");
            }

            @Override
            public void keyTyped(KeyEvent e) {
                char validar = e.getKeyChar();

                if (Character.isDigit(validar)) {

                    e.consume();
                    JOptionPane.showMessageDialog(null, "Solo debe ingresar letras");

                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
    }

    public static void cargarComboEstados(JComboBox combo) {
        EstadoSql est = new EstadoSql();
        ArrayList<Estado> lista = est.listar();

        DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();

        modelCombo.addElement("Seleccione");
        lista.forEach((aux) -> {
            modelCombo.addElement(aux);
        });

        combo.setModel(modelCombo);
    }

    public static void cargarComboEditoriales(JComboBox combo) {
        EditorialSql est = new EditorialSql();
        ArrayList<Editorial> lista = est.listar();

        DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();

        modelCombo.addElement("Seleccione");
        lista.forEach((aux) -> {
            modelCombo.addElement(aux);
        });

        combo.setModel(modelCombo);
    }

    public static void cargarComboLibros(JComboBox combo) {
        LibroSql est = new LibroSql();
        ArrayList<Libro> lista = est.listar();

        DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();

        modelCombo.addElement("Seleccione");
        lista.forEach((aux) -> {
            modelCombo.addElement(aux);
        });

        combo.setModel(modelCombo);
    }

    public static Boolean validarRut(String RUT) {
        Boolean lDevuelve = false;
        try {
            int Ult = RUT.length();
            int Largo = RUT.length() - 3;
            int Constante = 2;
            int Suma = 0;
            int Digito = 0;

            for (int i = Largo; i >= 0; i--) {

                Suma = Suma + Integer.parseInt(RUT.substring(i, i + 1)) * Constante;
                Constante = Constante + 1;
                if (Constante == 8) {
                    Constante = 2;
                }
            }
            String Ultimo = RUT.substring(Ult - 1).toUpperCase();
            Digito = 11 - (Suma % 11);
            if (Digito == 10 && Ultimo.equals("K")) {
                lDevuelve = true;
            } else {
                if (Digito == 11 && Ultimo.equals("0")) {
                    lDevuelve = true;
                } else {
                    if (Digito == Integer.parseInt(Ultimo)) {
                        lDevuelve = true;
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Su rut debe estar compuesto por numero y un guión");
        }
        return lDevuelve;
    }

    public static Boolean validarMail(String mail) {
        boolean est = false;
        String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@"
                + "[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(mail);
        if (matcher.matches()) {
            est = true;
        }
        return est;
    }

    public static void cargarComboDirecciones(JComboBox combo) {
        DireccionSql est = new DireccionSql();
        ArrayList<Direccion> lista = est.listar();

        DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();

        modelCombo.addElement("Seleccione");
        lista.forEach((aux) -> {
            modelCombo.addElement(aux);
        });

        combo.setModel(modelCombo);
    }

    public static void cargarComboNumeros(JComboBox combo) {
        TelefonoSql est = new TelefonoSql();
        ArrayList<Telefono> lista = est.listar();

        DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();

        modelCombo.addElement("Seleccione");
        lista.forEach((aux) -> {
            modelCombo.addElement(aux);
        });

        combo.setModel(modelCombo);
    }

    public static void cargarComboMetodo(JComboBox combo) {
        MetodoPagoSql est = new MetodoPagoSql();
        ArrayList<MetodoPago> lista = est.listar();

        DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();

        modelCombo.addElement("Seleccione");
        lista.forEach((aux) -> {
            modelCombo.addElement(aux);
        });

        combo.setModel(modelCombo);
    }

    public static void cargarComboClientes(JComboBox combo) {
        ClienteSql est = new ClienteSql();
        ArrayList<Cliente> lista = est.listar();

        DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();

        modelCombo.addElement("Seleccione");
        lista.forEach((aux) -> {
            modelCombo.addElement(aux);
        });

        combo.setModel(modelCombo);
    }

    public static void cargarComboTrabajadores(JComboBox combo) {
        TrabajadorSql est = new TrabajadorSql();
        ArrayList<Trabajador> lista = est.listar();

        DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();

        modelCombo.addElement("Seleccione");
        lista.forEach((aux) -> {
            modelCombo.addElement(aux);
        });

        combo.setModel(modelCombo);
    }

    public static void cargarComboBoletas(JComboBox combo) { 
        BoletaSql est = new BoletaSql();
        ArrayList<Boletas> lista = est.listar();

        DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();

        modelCombo.addElement("Seleccione");
        lista.forEach((aux) -> {
            modelCombo.addElement(aux);
        });

        combo.setModel(modelCombo);
    }

    public static void cargarComboDistribudiores(JComboBox combo) { 
        DistribuidorSql est = new DistribuidorSql();
        ArrayList<Distribuidor> lista = est.listar();

        DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();

        modelCombo.addElement("Seleccione");
        lista.forEach((aux) -> {
            modelCombo.addElement(aux);
        });

        combo.setModel(modelCombo);
    }

    public static void cargarComboFactura(JComboBox combo) { 
        FacturasSql est = new FacturasSql();
        ArrayList<Facturas> lista = est.listar();

        DefaultComboBoxModel modelCombo = new DefaultComboBoxModel();

        modelCombo.addElement("Seleccione");
        lista.forEach((aux) -> {
            modelCombo.addElement(aux);
        });

        combo.setModel(modelCombo);
    }
}

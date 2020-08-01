/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADORES;

import CONTROLADORES.personas.ControladorAutor;
import CONTROLADORES.ventas.ControladorBoleta;
import CONTROLADORES.personas.ControladorCliente;
import CONTROLADORES.compra.ControladorCompra;
import CONTROLADORES.personas.datos.ControladorCorreo;
import CONTROLADORES.personas.datos.ControladorDireccion;
import CONTROLADORES.personas.ControladorDistribuidor;
import CONTROLADORES.libros.ControladorEditorial;
import CONTROLADORES.libros.ControladorEstado;
import CONTROLADORES.compra.ControladorFactura;
import CONTROLADORES.libros.ControladorIdioma;
import CONTROLADORES.libros.ControladorLibro;
import CONTROLADORES.ventas.ControladorMedioPago;
import CONTROLADORES.personas.datos.ControladorTelefono;
import CONTROLADORES.personas.ControladorTrabajador;
import CONTROLADORES.ventas.ControladorVentas;
import CONTROLADORES.arriendos.ControladorArriendos;
import MODELOS.MODELOCLASES.arriendos.Arriendos;
import MODELOS.MODELOCLASES.compras.Compras;
import MODELOS.MODELOCLASES.compras.Facturas;
import MODELOS.MODELOCLASES.libros.Autor;
import MODELOS.MODELOCLASES.libros.Editorial;
import MODELOS.MODELOCLASES.libros.Estado;
import MODELOS.MODELOCLASES.libros.Idioma;
import MODELOS.MODELOCLASES.libros.Libro;
import MODELOS.MODELOCLASES.personas.Cliente;
import MODELOS.MODELOCLASES.personas.Distribuidor;
import MODELOS.MODELOCLASES.personas.Trabajador;
import MODELOS.MODELOCLASES.personas.datos.Correo;
import MODELOS.MODELOCLASES.personas.datos.Direccion;
import MODELOS.MODELOCLASES.personas.datos.Telefono;
import MODELOS.MODELOCLASES.ventas.Boletas;
import MODELOS.MODELOCLASES.ventas.MetodoPago;
import MODELOS.MODELOCLASES.ventas.Ventas;
import MODELOS.SQL.arriendos.ArriendosSql;
import MODELOS.SQL.compras.ComprasSql;
import MODELOS.SQL.compras.FacturasSql;
import MODELOS.SQL.libro.AutorSql;
import MODELOS.SQL.libro.EditorialSql;
import MODELOS.SQL.libro.EstadoSql;
import MODELOS.SQL.libro.IdiomaSql;
import MODELOS.SQL.libro.LibroSql;
import MODELOS.SQL.personas.ClienteSql;
import MODELOS.SQL.personas.DistribuidorSql;
import MODELOS.SQL.personas.TrabajadorSql;
import MODELOS.SQL.personas.datos.CorreoSql;
import MODELOS.SQL.personas.datos.DireccionSql;
import MODELOS.SQL.personas.datos.TelefonoSql;
import MODELOS.SQL.ventas.BoletaSql;
import MODELOS.SQL.ventas.MetodoPagoSql;
import MODELOS.SQL.ventas.VentasSql;
import VISTAS.libros.GestionarAutor;
import VISTAS.libros.GestionarEditorial;
import VISTAS.libros.GestionarEstado;
import VISTAS.libros.GestionarIdioma;
import VISTAS.libros.GestionarLibro;
import VISTAS.MenuInicio;
import VISTAS.arriendos.GestionarArriendos;
import VISTAS.compras.GestionarFacturas;
import VISTAS.compras.GestionarCompras;
import VISTAS.personas.GestionarClientes;
import VISTAS.personas.GestionarDistribuidores;
import VISTAS.personas.GestionarTrabajadores;
import VISTAS.personas.datos.GestionarCorreos;
import VISTAS.personas.datos.GestionarDirecciones;
import VISTAS.personas.datos.GestionarTelefonos;
import VISTAS.ventas.GestionarBoletas;
import VISTAS.ventas.GestionarMedioPago;
import VISTAS.ventas.GestionarVentas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel Huenul
 */
public class ControladorPrincipal implements ActionListener {

    private MenuInicio vista;

    public ControladorPrincipal(MenuInicio vista) {
        this.vista = vista;
        this.vista.Submenu_salirPorgrama.addActionListener(this);
        this.vista.Submenu_GestionarLibro.addActionListener(this);
        this.vista.Submenu_GestionarEstado.addActionListener(this);
        this.vista.Submenu_editoriales.addActionListener(this);
        this.vista.Submenu_Autores.addActionListener(this);
        this.vista.submenu_idioma.addActionListener(this);
        this.vista.submenu_distribuidores.addActionListener(this);
        this.vista.Submenu_clientes.addActionListener(this);
        this.vista.Submenu_trabajadores.addActionListener(this);
        this.vista.submenu_telefonos.addActionListener(this);
        this.vista.submenu_direcciones.addActionListener(this);
        this.vista.Submenu_correos.addActionListener(this); 
        this.vista.submenu_arriendos.addActionListener(this); 
        this.vista.submenu_boleta.addActionListener(this); 
        this.vista.submenu_mediopago.addActionListener(this); 
        this.vista.submenu_ventas.addActionListener(this); 
        this.vista.submenu_facturas.addActionListener(this); 
        this.vista.submenu_compras.addActionListener(this); 


        muestrate();
    }

    public void muestrate() {
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Biblioteca Fastdevelopment");
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(this.vista.Submenu_salirPorgrama)) {
            salirPorgrama();
        } 

        if (ae.getSource().equals(this.vista.Submenu_GestionarEstado)) {
            irGestionEstado();
        }

        if (ae.getSource().equals(this.vista.Submenu_editoriales)) {
            irGestionEditoriales();
        }

        if (ae.getSource().equals(this.vista.Submenu_Autores)) {
            irGestionAutores();
        }

        if (ae.getSource().equals(this.vista.Submenu_GestionarLibro)) {
            irGestionLibro();
        }

        if (ae.getSource().equals(this.vista.submenu_idioma)) {
            irGestionarIdioma();
        }

        if (ae.getSource().equals(this.vista.submenu_distribuidores)) {
            irGestionarDistribuidores();
        }

        if (ae.getSource().equals(this.vista.Submenu_clientes)) {
            irGestionarClientes();
        }

        if (ae.getSource().equals(this.vista.Submenu_trabajadores)) {
            irGestionarTrabajadores();
        }

        if (ae.getSource().equals(this.vista.submenu_telefonos)) {
            irGestionarTelefonos();
        }

        if (ae.getSource().equals(this.vista.submenu_direcciones)) {
            irGestionarDirecciones();
        }

        if (ae.getSource().equals(this.vista.Submenu_correos)) {
            irGestionarCorreos();
        }

        if (ae.getSource().equals(this.vista.submenu_arriendos)) {
            irGestionarArriendos();
        }

        if (ae.getSource().equals(this.vista.submenu_boleta)) {
            irGestionarBoleta();
        }

        if (ae.getSource().equals(this.vista.submenu_mediopago)) {
            irGestionarMedioPago();
        }

        if (ae.getSource().equals(this.vista.submenu_ventas)) {
            irGestionarVentas();
        }

        if (ae.getSource().equals(this.vista.submenu_facturas)) {
            irGestionarFactura();
        }

        if (ae.getSource().equals(this.vista.submenu_compras)) {
            irGestionarCompras();
        }
    }
    
    private void salirPorgrama() {
        int opt = JOptionPane.showConfirmDialog(null, "Desea salir del programa");
        if (opt == JOptionPane.YES_OPTION) {
            vista.dispose();
        }
    }

    private void irGestionEstado() {
        Estado est = new Estado();
        EstadoSql estSql = new EstadoSql();
        GestionarEstado viewEst = new GestionarEstado();

        ControladorEstado ctrl = new ControladorEstado(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionEditoriales() {
        Editorial est = new Editorial();
        EditorialSql estSql = new EditorialSql();
        GestionarEditorial viewEst = new GestionarEditorial();

        ControladorEditorial ctrl = new ControladorEditorial(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionAutores() {
        Autor est = new Autor();
        AutorSql estSql = new AutorSql();
        GestionarAutor viewEst = new GestionarAutor();

        ControladorAutor ctrl = new ControladorAutor(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionLibro() {
        Libro est = new Libro();
        LibroSql estSql = new LibroSql();
        GestionarLibro viewEst = new GestionarLibro();

        ControladorLibro ctrl = new ControladorLibro(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionarIdioma() {
        Idioma est = new Idioma();
        IdiomaSql estSql = new IdiomaSql();
        GestionarIdioma viewEst = new GestionarIdioma();

        ControladorIdioma ctrl = new ControladorIdioma(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionarClientes() {
        Cliente est = new Cliente();
        ClienteSql estSql = new ClienteSql();
        GestionarClientes viewEst = new GestionarClientes();

        ControladorCliente ctrl = new ControladorCliente(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionarDistribuidores() {
        Distribuidor est = new Distribuidor();
        DistribuidorSql estSql = new DistribuidorSql();
        GestionarDistribuidores viewEst = new GestionarDistribuidores();

        ControladorDistribuidor ctrl = new ControladorDistribuidor(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionarTrabajadores() {
        Trabajador est = new Trabajador();
        TrabajadorSql estSql = new TrabajadorSql();
        GestionarTrabajadores viewEst = new GestionarTrabajadores();

        ControladorTrabajador ctrl = new ControladorTrabajador(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionarTelefonos() {
        Telefono est = new Telefono();
        TelefonoSql estSql = new TelefonoSql();
        GestionarTelefonos viewEst = new GestionarTelefonos();

        ControladorTelefono ctrl = new ControladorTelefono(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionarDirecciones() {
        Direccion est = new Direccion();
        DireccionSql estSql = new DireccionSql();
        GestionarDirecciones viewEst = new GestionarDirecciones();

        ControladorDireccion ctrl = new ControladorDireccion(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionarCorreos() {
        Correo est = new Correo();
        CorreoSql estSql = new CorreoSql();
        GestionarCorreos viewEst = new GestionarCorreos();

        ControladorCorreo ctrl = new ControladorCorreo(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionarArriendos() {
        Arriendos est = new Arriendos();
        ArriendosSql estSql = new ArriendosSql();
        GestionarArriendos viewEst = new GestionarArriendos();

        ControladorArriendos ctrl = new ControladorArriendos(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionarBoleta() {
        Boletas est = new Boletas();
        BoletaSql estSql = new BoletaSql();
        GestionarBoletas viewEst = new GestionarBoletas();

        ControladorBoleta ctrl = new ControladorBoleta(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionarMedioPago() {
        MetodoPago est = new MetodoPago();
        MetodoPagoSql estSql = new MetodoPagoSql();
        GestionarMedioPago viewEst = new GestionarMedioPago();

        ControladorMedioPago ctrl = new ControladorMedioPago(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionarVentas() { 
        Ventas est = new Ventas();
        VentasSql estSql = new VentasSql();
        GestionarVentas viewEst = new GestionarVentas();

        ControladorVentas ctrl = new ControladorVentas(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionarFactura() {
        Facturas est = new Facturas();
        FacturasSql estSql = new FacturasSql();
        GestionarFacturas viewEst = new GestionarFacturas();

        ControladorFactura ctrl = new ControladorFactura(est, estSql, viewEst);

        this.vista.dispose();
    }

    private void irGestionarCompras() {
        Compras est = new Compras();
        ComprasSql estSql = new ComprasSql();
        GestionarCompras viewEst = new GestionarCompras();

        ControladorCompra ctrl = new ControladorCompra(est, estSql, viewEst);

        this.vista.dispose();
    }

    

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.compras;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.compras.Compras;
import MODELOS.MODELOCLASES.compras.Facturas;
import MODELOS.MODELOCLASES.libros.Libro;
import MODELOS.MODELOCLASES.personas.Distribuidor;
import MODELOS.MODELOCLASES.personas.datos.Direccion;
import MODELOS.MODELOCLASES.personas.datos.Telefono;
import MODELOS.MODELOCLASES.ventas.MetodoPago;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class ComprasSql extends ConecctSql {

    private PreparedStatement ps;
    private ResultSet rs;
    
    public ArrayList<Compras> listar() {
        Connection con = getConection();
        ArrayList<Compras> lista = new ArrayList<>();
        Compras obj;  
        try {

            String sql = "SELECT * FROM compras comp INNER JOIN distribuidores dis ON dis.idDistribuidore = comp.Distribuidores_idDistribuidore INNER JOIN facturas fac ON fac.idFactura = comp.Facturas_idFactura";
            con = getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Compras(); 
                //idCompra	codigoCompra	Facturas_idFactura	
                //Distribuidores_idDistribuidore	idDistribuidore	rut	
                //nombre_empresa	anno_sociedad	Telefonos_idTelefono	
                //Direcciones_idDireccion	
                //idFactura	folio	precio_neto	
                //precio_iva	costo_iva	fecha_compra	MetodosPagos_idMetodoPago
                obj.setId(rs.getInt("idCompra"));
                obj.setCodigo_compra(String.valueOf(rs.getInt("codigoCompra")));
                obj.setDistribuidor(new Distribuidor(
                        rs.getInt("idDistribuidore"), 
                        rs.getString("rut"), 
                        rs.getString("nombre_empresa"), 
                        rs.getDate("anno_sociedad"), 
                        new Telefono(rs.getInt("Telefonos_idTelefono"), ""), new Direccion(rs.getInt("Direcciones_idDireccion"), "")));
                obj.setFactura(new Facturas(
                        rs.getInt("idFactura"), 
                        rs.getInt("folio"), 
                        rs.getFloat("precio_neto"), 
                        rs.getFloat("precio_iva"), 
                        rs.getFloat("costo_iva"), 
                        rs.getString("fecha_compra"), 
                        new MetodoPago(rs.getInt("MetodosPagos_idMetodoPago"), "")));  

                lista.add(obj);
            }

        } catch (Exception e) {
            System.err.println("error: CATCH listar: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error: Finally listar" + e);
            }
        }

        return lista;
    }

    //codigoCompra
    public int VerificarCodigo(Compras modelo) {
        Connection con = null;

        try {

            String sql = "SELECT COUNT(codigoCompra) FROM compras WHERE codigoCompra = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, modelo.getCodigo_compra());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("COUNT(codigoCompra)");  
            }

            return -1;

        } catch (Exception e) {
            System.err.println("error CATCH VerificarCodigo :" + e);
            return -1;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally VerificarCodigo :" + e);
            }
        }
    }

    public boolean INSERT(Compras obj) {
        Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `compras`(`idCompra`, `codigoCompra`, `Facturas_idFactura`, `Distribuidores_idDistribuidore`) "
                    + "VALUES (0,?,?,?)";
            ps = con.prepareStatement(sql); 
            ps.setString(1, obj.getCodigo_compra());
            ps.setInt(2, obj.getFactura().getId());
            ps.setInt(3, obj.getDistribuidor().getId()); 

            int result = ps.executeUpdate();

            return result > 0;

        } catch (SQLException e) {
            System.err.println("error CATCH INSERT:" + e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("error finally INSERT:" + e);
            }
        }
    }

    public boolean SELECT(Compras obj) {
        Connection con = null;

        try {

            String sql = "SELECT * FROM compras comp INNER JOIN distribuidores dis ON dis.idDistribuidore = comp.Distribuidores_idDistribuidore INNER JOIN facturas fac ON fac.idFactura = comp.Facturas_idFactura WHERE idCompra = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getId());
            rs = ps.executeQuery();

            if (rs.next()) {
                obj.setId(rs.getInt("idCompra"));
                obj.setCodigo_compra(String.valueOf(rs.getInt("codigoCompra")));
                obj.setDistribuidor(new Distribuidor(
                        rs.getInt("idDistribuidore"), 
                        rs.getString("rut"), 
                        rs.getString("nombre_empresa"), 
                        rs.getDate("anno_sociedad"), 
                        new Telefono(rs.getInt("Telefonos_idTelefono"), ""), new Direccion(rs.getInt("Direcciones_idDireccion"), "")));
                obj.setFactura(new Facturas(
                        rs.getInt("idFactura"), 
                        rs.getInt("folio"), 
                        rs.getFloat("precio_neto"), 
                        rs.getFloat("precio_iva"), 
                        rs.getFloat("costo_iva"), 
                        rs.getString("fecha_compra"), 
                        new MetodoPago(rs.getInt("MetodosPagos_idMetodoPago"), "")));   
                return true;
            }

            return false;

        } catch (Exception e) {
            System.err.println("error CATCH SELECT desde:" + e);
            return false;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally SELECT desde:" + e);
            }
        }
    }

    public boolean REMOVE(Compras obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `compras` WHERE idCompra = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getId()); 
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("errorREMOVE:" + e);
            return false;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally REMOVE:" + e);
            }
        }
    }

    public boolean UPDATE(Compras obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            String sql = "UPDATE `compras` SET "
                    + "`codigoCompra`=?,"
                    + "`Facturas_idFactura`=?,"
                    + "`Distribuidores_idDistribuidore`=? "
                    + "WHERE `idCompra`=?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getCodigo_compra());
            ps.setInt(2, obj.getFactura().getId());
            ps.setInt(3, obj.getDistribuidor().getId());  
            
            ps.setInt(4, obj.getId());

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("error CATCH UPDATE:" + e);
            return false;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally UPDATE:" + e);
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.compras;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.compras.Facturas;
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
public class FacturasSql extends ConecctSql {

    private PreparedStatement ps;
    private ResultSet rs;

    public ArrayList<Facturas> listar() {
        Connection con = getConection();
        ArrayList<Facturas> lista = new ArrayList<>();
        Facturas obj; 

        try {

            String sql = "SELECT * FROM facturas fac INNER JOIN metodospagos mt ON fac.MetodosPagos_idMetodoPago = mt.idMetodoPago";
            con = getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Facturas(); 
                //idFactura	folio	precio_neto	
                //precio_iva	costo_iva	fecha_compra	
                //MetodosPagos_idMetodoPago	idMetodoPago	metodo	 
                obj.setId(rs.getInt("idFactura"));
                obj.setFolio(rs.getInt("folio"));
                obj.setPrecio_neto(rs.getFloat("precio_neto"));
                obj.setPrecio_iva(rs.getFloat("precio_iva"));
                obj.setCosto_iva(rs.getFloat("costo_iva"));
                obj.setFecha_compra(rs.getString("fecha_compra")); 
                obj.setMeotod_pago(new MetodoPago(rs.getInt("idMetodoPago"), rs.getString("metodo")));
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

    public int VerificarFolio(Facturas modelo) {
        Connection con = null;

        try {

            String sql = "SELECT COUNT(folio) FROM facturas WHERE folio = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, modelo.getFolio());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("COUNT(folio)");  
            }

            return -1;

        } catch (Exception e) {
            System.err.println("error CATCH VerificarFolio :" + e);
            return -1;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally VerificarFolio :" + e);
            }
        }
    }

    public boolean INSERT(Facturas obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `facturas`(`idFactura`, `folio`, `precio_neto`, `precio_iva`, `costo_iva`, `fecha_compra`, `MetodosPagos_idMetodoPago`) "
                    + "VALUES (0,?,?,?,?,CURRENT_TIMESTAMP,?)";
            ps = con.prepareStatement(sql); 
            ps.setInt(1, obj.getFolio());
            ps.setFloat(2, obj.getPrecio_neto());
            ps.setFloat(3, obj.getPrecio_iva());
            ps.setFloat(4, obj.getCosto_iva());
            ps.setFloat(5, obj.getMeotod_pago().getId()); 

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

    public boolean SELECT(Facturas obj) {
        com.mysql.jdbc.Connection con = null;

        try {

            String sql = "SELECT * FROM facturas fac INNER JOIN metodospagos mt ON fac.MetodosPagos_idMetodoPago = mt.idMetodoPago WHERE idFactura = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getId());
            rs = ps.executeQuery();

            if (rs.next()) {
                obj.setId(rs.getInt("idFactura"));
                obj.setFolio(rs.getInt("folio"));
                obj.setPrecio_neto(rs.getFloat("precio_iva"));
                obj.setCosto_iva(rs.getFloat("costo_iva"));
                obj.setFecha_compra(rs.getString("fecha_compra")); 
                obj.setMeotod_pago(new MetodoPago(rs.getInt("idMetodoPago"), rs.getString("metodo")));              
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

    public boolean REMOVE(Facturas obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `facturas` WHERE idFactura = ?";
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

    public boolean UPDATE(Facturas obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            String sql = "UPDATE `facturas` SET "
                    + "`folio` = ?,"
                    + "`precio_neto` = ?,"
                    + "`precio_iva` = ?,"
                    + "`costo_iva` = ?," 
                    + "`MetodosPagos_idMetodoPago` = ? "
                    + "WHERE `idFactura` = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getFolio());
            ps.setFloat(2, obj.getPrecio_neto());
            ps.setFloat(3, obj.getPrecio_iva()); 
            ps.setFloat(4, obj.getCosto_iva()); 
            ps.setInt(5, obj.getMeotod_pago().getId());  
            ps.setInt(6, obj.getId());

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

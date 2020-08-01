/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.ventas;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.libros.Editorial;
import MODELOS.MODELOCLASES.libros.Estado;
import MODELOS.MODELOCLASES.ventas.Boletas;
import MODELOS.MODELOCLASES.ventas.MetodoPago;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class BoletaSql extends ConecctSql {

    private PreparedStatement ps;
    private ResultSet rs;

    public ArrayList<Boletas> listar() {
        Connection con = getConection();
        ArrayList<Boletas> lista = new ArrayList<>();
        Boletas obj;
        try {

            String sql = "SELECT * FROM boletas bol INNER JOIN metodospagos mp ON mp.idMetodoPago = bol.MetodosPagos_idMetodoPago";
            con = getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                //idBoleta	folio	precio_neto	costo_iva	
                //precio_iva	fecha_venta	MetodosPagos_idMetodoPago	
                //idMetodoPago	metodo	
                obj = new Boletas();

                obj.setId(rs.getInt("idBoleta"));
                obj.setFolio(rs.getInt("folio"));
                obj.setPrecio_neto(rs.getFloat("precio_neto"));
                obj.setCosto_iva(rs.getInt("costo_iva"));
                obj.setPrecio_iva(rs.getFloat("precio_iva"));
                obj.setFecha_venta(rs.getString("fecha_venta"));
                obj.setMetodo_pago(new MetodoPago(rs.getInt("idMetodoPago"), rs.getString("metodo")));

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

    public boolean INSERT(Boletas obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `boletas`(`idBoleta`, `folio`, `precio_neto`, `costo_iva`, `precio_iva`, `fecha_venta`, `MetodosPagos_idMetodoPago`)"
                    + "VALUES (0,?,?,?,?,CURRENT_TIMESTAMP,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getFolio());
            ps.setFloat(2, obj.getPrecio_neto());
            ps.setFloat(3, obj.getCosto_iva());
            ps.setFloat(4, obj.getPrecio_iva());
            ps.setInt(5, obj.getMetodo_pago().getId());

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

    public boolean SELECT(Boletas obj) {
        com.mysql.jdbc.Connection con = null;

        try {

            String sql = "SELECT * FROM boletas bol "
                    + "INNER JOIN metodospagos mp ON mp.idMetodoPago = bol.MetodosPagos_idMetodoPago "
                    + "WHERE idBoleta = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getId());
            rs = ps.executeQuery();
            //idBoleta	folio	precio_neto	costo_iva	
            //precio_iva	fecha_venta	MetodosPagos_idMetodoPago	
            //idMetodoPago	metodo	
            if (rs.next()) {

                obj.setId(rs.getInt("idBoleta"));
                obj.setFolio(rs.getInt("folio"));
                obj.setPrecio_neto(rs.getFloat("precio_neto"));
                obj.setCosto_iva(rs.getInt("costo_iva"));
                obj.setPrecio_iva(rs.getFloat("precio_iva"));
                obj.setFecha_venta(rs.getString("fecha_venta"));
                obj.setMetodo_pago(new MetodoPago(rs.getInt("idMetodoPago"), rs.getString("metodo")));

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
    //SELECT * FROM `boletas` WHERE folio = 1
    

    public int Verificar(Boletas obj) {
        com.mysql.jdbc.Connection con = null;

        try {

            String sql = "SELECT COUNT(folio) FROM boletas WHERE folio = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getFolio());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("COUNT(folio)");  
            }

            return -1;

        } catch (Exception e) {
            System.err.println("error CATCH BUSCAR_ID desde C_LIBRO:" + e);
            return -1;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally BUSCAR_ID desde C_LIBRO:" + e);
            }
        }
    }

    public boolean REMOVE(Boletas obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `boletas` WHERE idBoleta = ?";
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

    public boolean UPDATE(Boletas obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            String sql = "UPDATE `boletas` SET "
                    + "`folio` = ?, "
                    + "`precio_neto` = ?,"
                    + "`costo_iva` = ?,"
                    + "`precio_iva` = ?,"
                    + "`MetodosPagos_idMetodoPago` = ?"
                    + " WHERE `idBoleta`= ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getFolio());
            ps.setFloat(2, obj.getPrecio_neto());
            ps.setFloat(3, obj.getCosto_iva()); 
            ps.setFloat(4, obj.getPrecio_iva());
            ps.setInt(5, obj.getMetodo_pago().getId());
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

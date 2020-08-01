/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.personas;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.libros.Libro;
import MODELOS.MODELOCLASES.personas.Cliente;
import com.mysql.jdbc.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class ClienteSql extends ConecctSql{
    private PreparedStatement ps;
    private ResultSet rs;
    
    public ArrayList<Cliente> listar() {
        Connection con = getConection();
        ArrayList<Cliente> lista = new ArrayList<>(); 
        Cliente obj;

        try {

            String sql = "SELECT * FROM `clientes`";
            con = getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Cliente();
                //`idCliente`, `rutCliente`, `nombre`, `apellido_p`, `apellido_m`, `fecha_contrato`
                obj.setId(rs.getInt("idCliente"));
                obj.setRut(rs.getString("rutCliente"));
                obj.setNombre(rs.getString("nombre"));
                obj.setApellido_p(rs.getString("apellido_p"));
                obj.setApellido_m(rs.getString("apellido_m"));
                obj.setAnno_sociedad(rs.getDate("fecha_contrato")); 

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

    public int VerificarRut(Cliente modelo) {
        Connection con = null;

        try { 
            String sql = "SELECT COUNT(rutCliente) FROM clientes WHERE rutCliente = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, modelo.getRut());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("COUNT(rutCliente)");  
            }

            return -1; 
        } catch (Exception e) {
            System.err.println("error CATCH VerificarRut:" + e);
            return -1;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally  VerificarRut:" + e);
            }
        }
    }
    
    public boolean INSERT(Cliente obj) {
        Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `clientes`(`idCliente`, `rutCliente`, `nombre`, `apellido_p`, `apellido_m`, `fecha_contrato`) "
                    + "VALUES (0,?,?,?,?,?)";
            ps = con.prepareStatement(sql); 
            ps.setString(1, obj.getRut());
            ps.setString(2, obj.getNombre());
            ps.setString(3, obj.getApellido_p());
            ps.setString(4, obj.getApellido_m());
            ps.setDate(5, (Date) obj.getAnno_sociedad()); 

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

    public boolean SELECT(Cliente obj) {
        com.mysql.jdbc.Connection con = null;

        try {

            String sql = "SELECT * FROM `clientes` WHERE idCliente = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getId());
            rs = ps.executeQuery();

            if (rs.next()) {
                ////`idCliente`, `rutCliente`, `nombre`, `apellido_p`, `apellido_m`, `fecha_contrato`
                obj.setId(rs.getInt("idCliente"));
                obj.setRut(rs.getString("rutCliente"));
                obj.setNombre(rs.getString("nombre"));
                obj.setApellido_p(rs.getString("apellido_p"));
                obj.setApellido_m(rs.getString("apellido_m"));
                obj.setAnno_sociedad(rs.getDate("fecha_contrato"));  
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

    public boolean REMOVE(Cliente obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `clientes` WHERE idCliente = ?";
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

    public boolean UPDATE(Cliente obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            String sql = "UPDATE `clientes` SET "
                    + "`rutCliente` = ?,"
                    + "`nombre` = ?,"
                    + "`apellido_p` = ?,"
                    + "`apellido_m` = ?,"
                    + "`fecha_contrato` = ?"
                    + " WHERE idCliente = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getRut());
            ps.setString(2, obj.getNombre());
            ps.setString(3, obj.getApellido_p()); 
            ps.setString(4, obj.getApellido_m());
            ps.setDate(5, (Date) obj.getAnno_sociedad());  
            ps.setInt(6, obj.getId());

            System.err.println("sql update cliente -> "+sql);
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

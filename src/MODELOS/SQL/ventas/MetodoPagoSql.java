/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.ventas;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.libros.Estado;
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
public class MetodoPagoSql extends ConecctSql{

    private PreparedStatement ps;
    private ResultSet rs;
    
    public ArrayList<MetodoPago> listar() {
        Connection con = getConection();
        ArrayList<MetodoPago> lista = new ArrayList<>();
        MetodoPago obj;

        try {

            String sql = "SELECT * FROM `metodospagos`";
            con = getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            //idMetodoPago	metodo 
            while (rs.next()) {
                obj = new MetodoPago();
                obj.setId(Integer.parseInt(rs.getString("idMetodoPago")));
                obj.setMetodo(rs.getString("metodo"));
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

   
    public boolean Verificar(MetodoPago obj) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT * FROM `metodospagos` WHERE idMetodoPago = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getId());
            rs = ps.executeQuery();
            //idMetodoPago	metodo 
            if(rs.next()){
                obj.setId(rs.getInt("idMetodoPago"));
                obj.setMetodo(rs.getString("metodo"));
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            System.err.println("error CATCH VerificarEstado:" + e); 
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally VerificarEstado:" + e);
            }
        }
        return false;
    }
    
    public String VerificarMetodo(MetodoPago obj) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT * FROM `metodospagos` WHERE metodo = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getMetodo());
            rs = ps.executeQuery();

            if(rs.next()){
                return rs.getString("metodo");
            }else{
                return "";
            }
        } catch (Exception e) {
            System.err.println("error CATCH VerificarMetodo:" + e); 
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally VerificarMetodo:" + e);
            }
        }
        return "";
    }

    public boolean INSERT(MetodoPago obj) {
        Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `metodospagos`(`idMetodoPago`, `metodo`) "
                    + "VALUES (0,?)";
            ps = con.prepareStatement(sql); 
            ps.setString(1, obj.getMetodo());

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

    public boolean UPDATE(String resp, int id) {
        Connection con = null;
        try {
            String sql = "UPDATE `metodospagos` SET `metodo` = ? "
                    + "WHERE `idMetodoPago` = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, resp);
            ps.setInt(2, id); 

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

    public boolean REMOVE(MetodoPago obj) {
        Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `metodospagos` WHERE idMetodoPago = ?";
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
    
}

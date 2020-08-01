/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.personas.datos;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.personas.datos.Direccion;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class DireccionSql extends ConecctSql{

    private PreparedStatement ps;
    private ResultSet rs;    
    
    public ArrayList<Direccion> listar() {
        Connection con = getConection();
        ArrayList<Direccion> lista = new ArrayList<>();
        Direccion obj;

        try {
            //	 
            String sql = "SELECT * FROM `direcciones`";
            con = getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Direccion();
                obj.setId(Integer.parseInt(rs.getString("idDireccion")));
                obj.setDireccion(rs.getString("direccion"));
                lista.add(obj);
            }

        } catch (SQLException e) {
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
    
    public boolean INSERT(Direccion obj) {
        Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `direcciones`(`idDireccion`, `direccion`) "
                    + "VALUES (0,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getDireccion());

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

    public String VerificarDirecc(Direccion obj) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT `direccion` FROM `direcciones` WHERE direccion = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getDireccion());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("direccion");
            } else {
                return "";
            }
        } catch (Exception e) {
            System.err.println("error CATCH VerificarNum:" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally VerificarNum:" + e);
            }
        }
        return "";
    }

    public int getIdDirecc(Direccion obj) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT * FROM `direcciones` WHERE direccion = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getDireccion());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("idDireccion");
            } else {
                return -1;
            }
        } catch (Exception e) {
            System.err.println("error CATCH getIdNum:" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally getIdNum:" + e);
            }
        }
        return -1;
    }
    
    public boolean UPDATE(String direcc, int id) {
        Connection con = null;
        try {
            String sql = "UPDATE `direcciones` SET `direccion` = ? WHERE idDireccion = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, direcc);
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

    public boolean REMOVE(Direccion obj) {
        Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `direcciones` WHERE direccion = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getDireccion());
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

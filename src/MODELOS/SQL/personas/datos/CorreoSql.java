/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.personas.datos;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.personas.datos.Correo;
import MODELOS.MODELOCLASES.personas.datos.Telefono;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class CorreoSql extends ConecctSql{

    private PreparedStatement ps;
    private ResultSet rs;
    
    public ArrayList<Correo> listar() {
        Connection con = getConection();
        ArrayList<Correo> lista = new ArrayList<>();
        Correo obj;

        try {
            //	 	idCorreo	correo	contraseña 
            String sql = "SELECT * FROM `correos`";
            con = getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Correo();
                obj.setId(Integer.parseInt(rs.getString("idCorreo")));
                obj.setCorreo(rs.getString("correo"));
                obj.setContrasenna(rs.getString("contraseña"));
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

    public boolean INSERT(Correo obj) {
        Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `correos`(`idCorreo`, `correo`, `contraseña`) "
                    + "VALUES (0,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getCorreo());
            ps.setString(2, obj.getContrasenna());

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

    public String VerificarCorreo(Correo obj) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT * FROM `correos` WHERE correo = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getCorreo());
            rs = ps.executeQuery();

            if (rs.next()) {
                obj.setId(rs.getInt("idCorreo"));
                obj.setCorreo(rs.getString("correo"));
                obj.setContrasenna(rs.getString("contraseña"));
                return rs.getString("correo");
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
    
    public int getIdCorreo(Correo obj) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT * FROM `correos` WHERE correo = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getCorreo());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("idCorreo");
            } else {
                return -1;
            }
        } catch (Exception e) {
            System.err.println("error CATCH getIdCorreo:" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally getIdCorreo:" + e);
            }
        }
        return -1;
    }
    
    public boolean UPDATE(Correo obj) {
        Connection con = null;
        try {
            String sql = "UPDATE `correos` SET `correo`=?,`contraseña`=? WHERE idCorreo = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getCorreo());
            ps.setString(2, obj.getContrasenna());
            ps.setInt(3, obj.getId()); 

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

    public boolean REMOVE(Correo obj) {
        Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `correos` WHERE idCorreo = ?";
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

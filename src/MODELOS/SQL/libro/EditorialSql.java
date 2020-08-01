/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.libro;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.libros.Editorial;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class EditorialSql extends ConecctSql{
    private PreparedStatement ps;
    private ResultSet rs;
    
     public ArrayList<Editorial> listar() {
        Connection con = getConection();
        ArrayList<Editorial> lista = new ArrayList<>();
        Editorial obj;

        try {

            String sql = "SELECT * FROM `editoriales`";
            con = getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Editorial();
                obj.setId(rs.getInt("idEditorial"));
                obj.setNombre(rs.getString("nombre"));
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
     
     public String Verificar(Editorial estado) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT `nombre` FROM `editoriales` WHERE `nombre` = ? ";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, estado.getNombre());
            rs = ps.executeQuery();

            if(rs.next()){
                return rs.getString("nombre");
            }else{
                return "";
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
        return "";
    }

    public boolean INSERT(Editorial obj) {
        Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `editoriales`(`idEditorial`, `nombre`) VALUES (?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, 0);
            ps.setString(2, obj.getNombre());

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

    public boolean REMOVE(Editorial obj) {
       Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `editoriales` WHERE  `nombre`=?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getNombre());
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

    public int getIdEditorial(Editorial modelo) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT * FROM `editoriales` WHERE `nombre` = ? ";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, modelo.getNombre());
            rs = ps.executeQuery();

            if(rs.next()){
                return rs.getInt("idEditorial");
            }else{
                return -1;
            }
        } catch (Exception e) {
            System.err.println("error CATCH getIdEditorial:" + e); 
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally getIdEditorial:" + e);
            }
        }
        return -1;
    }

    public boolean UPDATE(String estado, int id) {
        Connection con = null;
        try {
            String sql = "UPDATE `editoriales` SET `nombre`=? WHERE idEditorial=?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, estado);
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
}

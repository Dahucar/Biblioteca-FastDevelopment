/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.libro;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.libros.Estado;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class EstadoSql extends ConecctSql {

    private PreparedStatement ps;
    private ResultSet rs;

    public boolean INSERT(Estado obj) {
        Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `estados`(`idEstado`, `estado`) VALUES (?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, 0);
            ps.setString(2, obj.getEstado());

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
    
    public boolean REMOVE(Estado obj) {
        Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `estados` WHERE  `estado`=?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getEstado());
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
    
    public boolean UPDATE(String estado, int id) {
        Connection con = null;
        try {
            String sql = "UPDATE `estados` SET `estado`=? WHERE idEstado=?";
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
    
    public int getIdEstado(Estado estado) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT * FROM `estados` WHERE `estado` = ? ";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, estado.getEstado());
            rs = ps.executeQuery();

            if(rs.next()){
                return rs.getInt("idEstado");
            }else{
                return -1;
            }
        } catch (Exception e) {
            System.err.println("error CATCH getIdEstado:" + e); 
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally getIdEstado:" + e);
            }
        }
        return -1;
    }
    
    public String VerificarEstado(Estado estado) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT `estado` FROM `estados` WHERE `estado` = ? ";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, estado.getEstado());
            rs = ps.executeQuery();

            if(rs.next()){
                return rs.getString("estado");
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

    public ArrayList<Estado> listar() {
        Connection con = getConection();
        ArrayList<Estado> lista = new ArrayList<>();
        Estado obj;

        try {

            String sql = "SELECT * FROM `estados`";
            con = getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Estado();
                obj.setId(Integer.parseInt(rs.getString("idEstado")));
                obj.setEstado(rs.getString("estado"));
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
}

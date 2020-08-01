/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.personas.datos;

import MODELOS.CONFIG_SQL.ConecctSql;
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
public class TelefonoSql extends ConecctSql {

    private PreparedStatement ps;
    private ResultSet rs;

    public ArrayList<Telefono> listar() {
        Connection con = getConection();
        ArrayList<Telefono> lista = new ArrayList<>();
        Telefono obj;

        try {
            //	 
            String sql = "SELECT * FROM `telefonos`";
            con = getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Telefono();
                obj.setId(Integer.parseInt(rs.getString("idTelefono")));
                obj.setNumero(rs.getString("numero_telefono"));
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

    public boolean INSERT(Telefono obj) {
        Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `telefonos`(`idTelefono`, `numero_telefono`) "
                    + "VALUES (0,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getNumero());

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

    public String VerificarNum(Telefono obj) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT numero_telefono FROM `telefonos` WHERE numero_telefono = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getNumero());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("numero_telefono");
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

    public int getIdNum(Telefono obj) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT * FROM `telefonos` WHERE numero_telefono = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getNumero());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("idTelefono");
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
    public boolean UPDATE(String num, int id) {
        Connection con = null;
        try {
            String sql = "UPDATE `telefonos` SET `numero_telefono`=? WHERE idTelefono = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, num);
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

    public boolean REMOVE(Telefono obj) {
        Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `telefonos` WHERE numero_telefono = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getNumero());
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

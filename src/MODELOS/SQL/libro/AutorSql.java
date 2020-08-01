/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.libro;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.libros.Autor;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class AutorSql extends ConecctSql {

    private PreparedStatement ps;
    private ResultSet rs;

    public ArrayList<Autor> listar() {
        Connection con = getConection();
        ArrayList<Autor> lista = new ArrayList<>();
        Autor obj;

        try {

            String sql = "SELECT * FROM `autores`";
            con = getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Autor();
                obj.setId(rs.getInt("idAutor"));
                obj.setNombre(rs.getString("nombre"));
                obj.setApellido_p(rs.getString("apellido_p"));
                obj.setApellido_m(rs.getString("apellido_m"));

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

    public String Verificar(Autor modelo) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT `idAutor`, `nombre`, `apellido_p`, `apellido_m` FROM `autores` "
                    + "WHERE `nombre` = ? "
                    + "AND `apellido_p` = ? "
                    + "AND `apellido_m` = ? ";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, modelo.getNombre());
            ps.setString(2, modelo.getApellido_p());
            ps.setString(3, modelo.getApellido_m());
            rs = ps.executeQuery();

            if (rs.next()) {
                String dato = rs.getString("nombre") + "" + rs.getString("apellido_p") + "" + rs.getString("apellido_m");
                modelo.setId(rs.getInt("idAutor"));
                modelo.setNombre(rs.getString("nombre"));
                modelo.setApellido_p(rs.getString("apellido_p"));
                modelo.setApellido_m(rs.getString("apellido_m"));
                System.out.println("Datos sql: " + dato);
                return dato;
            } else {
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

    public boolean INSERT(Autor obj) {
        Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `autores`(`idAutor`, `nombre`, `apellido_p`, `apellido_m`) "
                    + "VALUES (?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, 0);
            ps.setString(2, obj.getNombre());
            ps.setString(3, obj.getApellido_p());
            ps.setString(4, obj.getApellido_m());

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

    public boolean REMOVE(Autor obj) {
        Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `autores` WHERE  `nombre`=? "
                    + "AND `apellido_p`=?"
                    + "AND `apellido_m`=?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getApellido_p());
            ps.setString(3, obj.getApellido_m());
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

    public int getIdAutor(Autor modelo) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT * FROM `autores` "
                    + "WHERE `nombre` = ? "
                    + "AND `apellido_p`=? "
                    + "AND `apellido_m`=?";;
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, modelo.getNombre());
            ps.setString(2, modelo.getApellido_p());
            ps.setString(3, modelo.getApellido_m());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("idAutor");
            } else {
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

    public boolean updateNombre(String nombre, int id) {
        Connection con = null;
        try {
            String sql = "UPDATE `autores` SET `nombre`= ? WHERE idAutor=?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
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

    public boolean updateApellidoP(String ape_p, int id) {
        Connection con = null;
        try {
            String sql = "UPDATE `autores` SET `apellido_p`=? WHERE idAutor=?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, ape_p);
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

    public boolean updateApellidoM(String ape_m, int id) {
        Connection con = null;
        try {
            String sql = "UPDATE `autores` SET `apellido_m`=? WHERE idAutor=?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, ape_m);
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

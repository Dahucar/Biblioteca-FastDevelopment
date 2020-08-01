/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.libro;

import MODELOS.CONFIG_SQL.ConecctSql; 
import MODELOS.MODELOCLASES.libros.Idioma;
import MODELOS.MODELOCLASES.libros.Libro;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class IdiomaSql extends ConecctSql {

    private PreparedStatement ps;
    private ResultSet rs;

    public ArrayList<Idioma> listar() {
        Connection con = getConection();
        ArrayList<Idioma> lista = new ArrayList<>();
        Idioma obj;

        try {

            String sql = "SELECT idi.idIdioma, idi.idioma, lib.idLibro, lib.num_serie, lib.nombre FROM idiomas idi INNER JOIN libros lib ON lib.idLibro = idi.Libros_idLibro";
            con = getConection();

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Idioma();
                obj.setId(rs.getInt("idi.idIdioma"));
                obj.setIdioma(rs.getString("idi.idioma"));

                Libro lib = new Libro();
                lib.setId(rs.getInt("lib.idLibro"));
                lib.setNumSerie(rs.getInt("lib.num_serie"));
                lib.setNombre(rs.getString("lib.nombre"));

                obj.setIdlibro(lib);

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

    public boolean verificar(Idioma modelo) {
        Connection con = null;
        try {

            String sql = "SELECT `idioma`, `Libros_idLibro` FROM `idiomas` WHERE idioma = ? AND Libros_idLibro = ?";
            con = getConection();

            ps = con.prepareStatement(sql);
            ps.setString(1, modelo.getIdioma());
            ps.setInt(2, modelo.getIdlibro().getId());
            rs = ps.executeQuery();

            return rs.next();
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

    public boolean update(String idioma, Idioma modelo) {
        Connection con = null;
        try {
            String sql = "UPDATE idiomas idi "
                    + "INNER JOIN libros lib ON lib.idLibro = idi.Libros_idLibro "
                    + "SET idi.idioma = ? "
                    + "WHERE idi.idioma = ? AND lib.idLibro = ? AND lib.num_serie = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, idioma);
            ps.setString(2, modelo.getIdioma());
            ps.setInt(3, modelo.getIdlibro().getId());
            ps.setInt(4, modelo.getIdlibro().getNumSerie());

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

    public boolean INSERT(Idioma modelo) {
        Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `idiomas`(`idIdioma`, `idioma`, `Libros_idLibro`) "
                    + "VALUES (0,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, modelo.getIdioma());
            ps.setInt(2, modelo.getIdlibro().getId());

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

    public Object verificarRegistro(Idioma modelo) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT idi.idioma, lib.num_serie FROM idiomas idi "
                    + "INNER JOIN libros lib ON idi.Libros_idLibro = lib.idLibro "
                    + "WHERE idi.idioma = ? AND lib.num_serie = ?";

            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, modelo.getIdioma());
            ps.setInt(2, modelo.getIdlibro().getNumSerie());
            rs = ps.executeQuery();

            if (rs.next()) {
                String datosql = rs.getString("idi.idioma") + "" + rs.getString("lib.num_serie");
                System.out.println("DATOSQL " + datosql);

                return datosql;
            } else {
                return "";
            }
        } catch (Exception e) {
            System.err.println("error CATCH verificarRegistro:" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally VerificarEstado:" + e);
            }
        }
        return "";
    }

    public boolean REMOVE(Idioma modelo) {
        Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `idiomas` WHERE idioma = ? AND Libros_idLibro = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, modelo.getIdioma());
            ps.setInt(2, modelo.getIdlibro().getId());
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

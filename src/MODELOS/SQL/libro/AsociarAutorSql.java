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
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class AsociarAutorSql extends ConecctSql {

    private PreparedStatement ps;
    private ResultSet rs;
    
    /*
        DELETE FROM `autores_de_libros` WHERE Autores_idAutor = ? AND Libros_idLibro = ?
    */
    public boolean insert(int idlib, int idaut) {
        Connection con = null;
        try {
            String sql = "INSERT INTO `autores_de_libros`(`idAutores_de_Libros`, `Autores_idAutor`, `Libros_idLibro`) "
                    + "VALUES (0,?,?)";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idaut);
            ps.setInt(2, idlib);

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("error CATCH insert:" + e);
            return false;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally insert:" + e);
            }
        }
    }
    
    public boolean delete(int idlib, int idaut) {
        Connection con = null;
        try {
            String sql = "DELETE FROM `autores_de_libros` WHERE Autores_idAutor = "+idaut+" AND Libros_idLibro = "+idlib+"";
            con = getConection();
            ps = con.prepareStatement(sql); 

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("error CATCH insert:" + e);
            return false;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally insert:" + e);
            }
        }
    }

    public int getCantidad(int id) {
        Connection con = getConection();
        int numero = 0;
        try {

            String sql = "SELECT COUNT(Libros_idLibro) FROM autores_de_libros WHERE Libros_idLibro=?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();
            while (rs.next()) {
                numero = rs.getInt("COUNT(Libros_idLibro)");
            }
            return numero;
        } catch (Exception e) {
            System.err.println("error: CATCH getCantidad: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error: Finally getCantidad" + e);
            }
        }

        return numero;
    }

    public int[] idAutoresSinAñadir(int idlibro, int cantidadRegistros) {
        Connection con = getConection();
        int listadoIds[] = new int[cantidadRegistros];

        try {

            String sql = "SELECT `Autores_idAutor` FROM `autores_de_libros` WHERE Libros_idLibro = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idlibro);

            rs = ps.executeQuery();
            int cont = 0;
            while (rs.next()) {

                listadoIds[cont] = rs.getInt("Autores_idAutor");

                cont++;
            }

        } catch (Exception e) {
            System.err.println("error: CATCH AutoresSinAñadir: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error: Finally AutoresSinAñadir" + e);
            }
        }

        return listadoIds;
    }

    public ArrayList<Autor> listadoAutoresAñadir(int[] listado, boolean igualdad, int id) {

        Connection con = getConection();
        ArrayList<Autor> lista = new ArrayList<>();
        Autor obj;

        try {
            con = getConection();
            String sql = "SELECT * FROM `autores` ";

            //si igualdad es true: se tienen que buscar las diferencias de ID
            //si iigualdad es false: se deben buscar las diferencias
            if (igualdad) {
                if (listado.length != 0) {
                    for (int i = 0; i < listado.length; i++) {
                        if (i == 0) {
                            sql += "WHERE idAutor != " + listado[i] + " ";
                        } else {
                            sql += "AND idAutor != " + listado[i] + " ";
                        }

                    }
                    System.out.println("SQL TRUE: " + sql); 
                }
            } else {
                sql = "SELECT aut.idAutor, aut.nombre, aut.apellido_p, aut.apellido_m FROM autores_de_libros autlib INNER JOIN libros lib ON autlib.Libros_idLibro = lib.idLibro INNER JOIN autores aut ON autlib.Autores_idAutor = aut.idAutor where Libros_idLibro = "+id+"";
                System.out.println("SQL FALSE: " + sql);
                
            }
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
            System.err.println("error: CATCH listadoAutoresAñadir: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error: Finally listadoAutoresAñadir" + e);
            }
        }

        return lista;
    }

    public ArrayList<Autor> listar() {
        Connection con = getConection();
        ArrayList<Autor> lista = new ArrayList<>();
        Autor obj;

        try {

            String sql = "SELECT * FROM `autores` ";

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
}

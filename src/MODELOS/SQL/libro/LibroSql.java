/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.libro;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.libros.Editorial;
import MODELOS.MODELOCLASES.libros.Estado;
import MODELOS.MODELOCLASES.libros.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class LibroSql extends ConecctSql {

    private PreparedStatement ps;
    private ResultSet rs;

    //`idLibro`, `num_serie`, `isbn`, `nombre`, `num_paginas`, 
    //`precio`, `Editoriales_idEditorial`, `Estados_idEstado`
    public ArrayList<Libro> listar() {
        Connection con = getConection();
        ArrayList<Libro> lista = new ArrayList<>();
        Libro obj;
        Editorial edito;
        Estado est;

        try {

            String sql = "SELECT lib.idLibro, lib.num_serie, lib.isbn, lib.nombre, lib.num_paginas, lib.precio, lib.stock, edito.idEditorial, edito.nombre, est.idEstado, est.estado FROM libros lib INNER JOIN editoriales edito ON lib.Editoriales_idEditorial = edito.idEditorial INNER JOIN estados est ON lib.Estados_idEstado = est.idEstado";
            con = getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Libro();
                edito = new Editorial();
                est = new Estado();
                //idLibro
                //num_serie
                //isbn
                //nombre
                //num_paginas
                //precio
                //stock
                //idEditorial
                //nombre
                //idEstado
                //estado
                obj.setId(Integer.parseInt(rs.getString("idLibro")));
                obj.setNumSerie(Integer.parseInt(rs.getString("num_serie")));
                obj.setIsbn(Integer.parseInt(rs.getString("isbn")));
                obj.setNombre(rs.getString("nombre"));
                obj.setNumPaginas(Integer.parseInt(rs.getString("num_paginas")));
                obj.setPrecio(Float.parseFloat(rs.getString("precio")));
                obj.setStock(rs.getInt("stock"));

                edito.setId(Integer.parseInt(rs.getString("idEditorial")));
                edito.setNombre(rs.getString("nombre"));

                est.setId(Integer.parseInt(rs.getString("idEstado")));
                est.setEstado(rs.getString("estado"));

                obj.setEditorial(edito);
                obj.setEstado(est);

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

    public int Verificar(Libro modelo) {
        com.mysql.jdbc.Connection con = null;

        try {

            String sql = "SELECT COUNT(num_serie) FROM libros WHERE num_serie = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, modelo.getNumSerie());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("COUNT(num_serie)");  
            }

            return -1;

        } catch (Exception e) {
            System.err.println("error CATCH BUSCAR_ID desde C_LIBRO:" + e);
            return -1;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally BUSCAR_ID desde C_LIBRO:" + e);
            }
        }
    }

    public boolean INSERT(Libro obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `libros`(`idLibro`, `num_serie`, `isbn`, `nombre`, `num_paginas`, `precio`, stock, `Editoriales_idEditorial`, `Estados_idEstado`) "
                    + "VALUES (?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, 0);
            ps.setInt(2, obj.getNumSerie());
            ps.setInt(3, obj.getIsbn());
            ps.setString(4, obj.getNombre());
            ps.setInt(5, obj.getNumPaginas());
            ps.setFloat(6, obj.getPrecio());
            ps.setInt(7, obj.getStock());
            ps.setInt(8, obj.getEditorial().getId());
            ps.setInt(9, obj.getEstado().getId());

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

    public boolean SELECT(Libro modelo) {
        com.mysql.jdbc.Connection con = null;

        try {

            String sql = "SELECT lib.idLibro, lib.num_serie, lib.isbn, lib.nombre, lib.num_paginas, lib.precio, lib.stock, edito.idEditorial, edito.nombre, est.idEstado, est.estado FROM libros lib INNER JOIN editoriales edito ON lib.Editoriales_idEditorial = edito.idEditorial INNER JOIN estados est ON lib.Estados_idEstado = est.idEstado WHERE lib.num_serie = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, modelo.getNumSerie());
            rs = ps.executeQuery();

            if (rs.next()) {
                
                modelo.setId(rs.getInt("lib.idLibro"));
                modelo.setNumSerie(rs.getInt("lib.num_serie"));
                modelo.setIsbn(rs.getInt("lib.isbn"));
                modelo.setNombre(rs.getString("lib.nombre"));
                modelo.setNumPaginas(rs.getInt("lib.num_paginas"));
                modelo.setPrecio(rs.getFloat("lib.precio"));
                modelo.setStock(rs.getInt("lib.stock"));
                
                Editorial edito = new Editorial();
                edito.setId(rs.getInt("edito.idEditorial"));
                edito.setNombre(rs.getString("edito.nombre"));
                
                Estado est = new Estado();
                est.setId(rs.getInt("est.idEstado"));
                est.setEstado(rs.getString("est.estado"));
                
                modelo.setEditorial(edito);
                modelo.setEstado(est);
                
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

    public boolean REMOVE(Libro obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `libros` WHERE num_serie = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getNumSerie()); 
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

    public boolean UPDATE(Libro obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            String sql = "UPDATE `libros` SET "
                    + "`num_serie`=?,"
                    + "`isbn`=?,"
                    + "`nombre`=?,"
                    + "`num_paginas`=?,"
                    + "`precio`=?,"
                    + "`stock`=?,"
                    + "`Editoriales_idEditorial`=?,"
                    + "`Estados_idEstado`=? "
                    + "WHERE `idLibro` = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getNumSerie());
            ps.setInt(2, obj.getIsbn());
            ps.setString(3, obj.getNombre()); 
            ps.setInt(4, obj.getNumPaginas());
            ps.setFloat(5, obj.getPrecio());
            ps.setInt(6, obj.getStock());
            
            ps.setInt(7, obj.getEditorial().getId());
            ps.setInt(8, obj.getEstado().getId());
            
            ps.setInt(9, obj.getId());

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

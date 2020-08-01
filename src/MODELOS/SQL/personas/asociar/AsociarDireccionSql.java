/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.personas.asociar;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.personas.datos.Direccion;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class AsociarDireccionSql extends ConecctSql {

    private PreparedStatement ps;
    private ResultSet rs;

    public boolean insert(int idTrab, int idDirecc) {
        Connection con = null;
        try {
            String sql = "INSERT INTO `direcciones_de_trabajadores`(`idDirecciones_de_Trabajadores`, `Direcciones_idDireccion`, `Trabajadores_idTrabajador`) "
                    + "VALUES (0,?,?)";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idDirecc);
            ps.setInt(2, idTrab);

            System.out.println(sql);
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

    public boolean insertCliente(int idCli, int idDirecc) {
        Connection con = null;
        try {
            String sql = "INSERT INTO `direcciones_de_clientes`(`idDirecciones_de_Clientes`, `Clientes_idCliente`, `Direcciones_idDireccion`) "
                    + "VALUES (0,?,?)";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCli);
            ps.setInt(2, idDirecc);

            System.out.println(sql);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("error CATCH insertCliente:" + e);
            return false;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally insertCliente:" + e);
            }
        }
    }

    public int getCantidad(int id) {
        Connection con = getConection();
        int numero = 0;
        try {

            String sql = "SELECT COUNT(Trabajadores_idTrabajador) FROM direcciones_de_trabajadores WHERE Trabajadores_idTrabajador = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();
            while (rs.next()) {
                numero = rs.getInt("COUNT(Trabajadores_idTrabajador)");
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

    public int getCantidadCliente(int id) {
        Connection con = getConection();
        int numero = 0;
        try {

            String sql = "SELECT COUNT(Clientes_idCliente) FROM direcciones_de_clientes WHERE Clientes_idCliente = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();
            while (rs.next()) {
                numero = rs.getInt("COUNT(Clientes_idCliente)");
            }
            return numero;
        } catch (Exception e) {
            System.err.println("error: CATCH getCantidadCliente: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error: Finally getCantidadCliente" + e);
            }
        }

        return numero;
    }

    public ArrayList<Direccion> listadoTelefonoAñadir(int[] listado, boolean igualdad, int id) {

        Connection con = getConection();
        ArrayList<Direccion> lista = new ArrayList<>();
        Direccion obj;

        try {
            //idCorreo	correo	contraseña 
            con = getConection();
            String sql = "SELECT * FROM `direcciones` ";

            //si igualdad es true: se tienen que buscar las diferencias de ID
            //si iigualdad es false: se deben buscar las diferencias
            if (igualdad) {
                if (listado.length != 0) {
                    for (int i = 0; i < listado.length; i++) {
                        if (i == 0) {
                            sql += "WHERE idDireccion != " + listado[i] + " ";
                        } else {
                            sql += "AND idDireccion != " + listado[i] + " ";
                        }

                    }
                    System.out.println("SQL TRUE: " + sql);
                }
            } else {
                sql = "SELECT direc.idDireccion, direc.direccion "
                        + "FROM direcciones_de_trabajadores direccTrab "
                        + "INNER JOIN direcciones direc ON direc.idDireccion = direccTrab.Direcciones_idDireccion "
                        + "INNER JOIN trabajadores trab ON trab.idTrabajador = direccTrab.Trabajadores_idTrabajador "
                        + "WHERE Trabajadores_idTrabajador = " + id + "";
                System.out.println("SQL FALSE: " + sql);

            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Direccion();
                obj.setId(rs.getInt("idDireccion"));
                obj.setDireccion(rs.getString("direccion"));
                lista.add(obj);
            }

        } catch (Exception e) {
            System.err.println("error: CATCH listadoTelefonoAñadir: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error: Finally listadoTelefonoAñadir" + e);
            }
        }

        return lista;
    }

    public ArrayList<Direccion> listadoTelefonoAñadirCliente(int[] listado, boolean igualdad, int id) {

        Connection con = getConection();
        ArrayList<Direccion> lista = new ArrayList<>();
        Direccion obj;

        try {
            //idCorreo	correo	contraseña 
            con = getConection();
            String sql = "SELECT * FROM `direcciones` ";

            //si igualdad es true: se tienen que buscar las diferencias de ID
            //si iigualdad es false: se deben buscar las diferencias
            if (igualdad) {
                if (listado.length != 0) {
                    for (int i = 0; i < listado.length; i++) {
                        if (i == 0) {
                            sql += "WHERE idDireccion != " + listado[i] + " ";
                        } else {
                            sql += "AND idDireccion != " + listado[i] + " ";
                        }

                    }
                    System.out.println("SQL TRUE: " + sql);
                }
            } else {
                sql = "SELECT direc.idDireccion, direc.direccion FROM direcciones_de_clientes direccCli INNER JOIN direcciones direc ON direc.idDireccion = direccCli.Direcciones_idDireccion INNER JOIN clientes cli ON cli.idCliente = direccCli.Clientes_idCliente WHERE Clientes_idCliente = " + id + "";
                System.out.println("SQL FALSE: " + sql);

            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Direccion();
                obj.setId(rs.getInt("idDireccion"));
                obj.setDireccion(rs.getString("direccion"));
                lista.add(obj);
            }

        } catch (Exception e) {
            System.err.println("error: CATCH listadoTelefonoAñadirCliente: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error: Finally listadoTelefonoAñadirCliente" + e);
            }
        }

        return lista;
    }

    public boolean VerificarDirecc(Direccion obj) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT * FROM `direcciones` WHERE idDireccion = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getId());
            rs = ps.executeQuery();

            if (rs.next()) {
                obj.setId(rs.getInt("idDireccion"));
                obj.setDireccion(rs.getString("direccion"));
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("error CATCH VerificarDirecc:" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally VerificarDirecc:" + e);
            }
        }
        return false;
    }

    public int[] idDireccionSinAñadir(int idDirecc, int cantidadRegistros) {
        Connection con = getConection();
        int listadoIds[] = new int[cantidadRegistros];

        try {

            String sql = "SELECT Direcciones_idDireccion FROM direcciones_de_trabajadores WHERE Trabajadores_idTrabajador = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idDirecc);

            rs = ps.executeQuery();
            int cont = 0;
            while (rs.next()) {

                listadoIds[cont] = rs.getInt("Direcciones_idDireccion");

                cont++;
            }

        } catch (Exception e) {
            System.err.println("error: CATCH idDireccionSinAñadir: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error: Finally idDireccionSinAñadir" + e);
            }
        }

        return listadoIds;
    }

    public int[] idDireccionSinAñadirCliente(int idDirecc, int cantidadRegistros) {
        Connection con = getConection();
        int listadoIds[] = new int[cantidadRegistros];

        try {

            String sql = "SELECT Direcciones_idDireccion FROM direcciones_de_clientes WHERE Clientes_idCliente = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idDirecc);

            rs = ps.executeQuery();
            int cont = 0;
            while (rs.next()) {

                listadoIds[cont] = rs.getInt("Direcciones_idDireccion");

                cont++;
            }

        } catch (Exception e) {
            System.err.println("error: CATCH idDireccionSinAñadirCliente: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error: Finally idDireccionSinAñadirCliente" + e);
            }
        }

        return listadoIds;
    }

    public boolean delete(int idTra, int idDirecc) {
        Connection con = null;
        try {
            String sql = "DELETE FROM direcciones_de_trabajadores WHERE Direcciones_idDireccion = " + idDirecc + " AND Trabajadores_idTrabajador = " + idTra + "";
            con = getConection();
            ps = con.prepareStatement(sql);

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("error CATCH delete:" + e);
            return false;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally delete:" + e);
            }
        }
    }

    public boolean deleteCliente(int idClie, int idDirecc) {
        Connection con = null;
        try {
            String sql = "DELETE FROM `direcciones_de_clientes` WHERE Clientes_idCliente = "+idClie+" AND Direcciones_idDireccion = "+idDirecc+"";
            con = getConection();
            ps = con.prepareStatement(sql);

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("error CATCH delete:" + e);
            return false;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally delete:" + e);
            }
        }
    }
}

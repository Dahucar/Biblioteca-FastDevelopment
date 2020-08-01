/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.personas.asociar;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.personas.datos.Telefono;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class AsociarNumeroSql extends ConecctSql{
    private PreparedStatement ps;
    private ResultSet rs;
    
    public boolean insert(int idTrab, int idTel) {
        Connection con = null;
        try {
            String sql = "INSERT INTO `telefonos_de_trabajadores`(`idTelefonos_de_Trabajadores`, `Trabajadores_idTrabajador`, `Telefonos_idTelefono`) "
                    + "VALUES (0, ?, ?)";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idTrab);
            ps.setInt(2, idTel);

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
    
    public boolean insertCliente(int idCli, int idTel) {
        Connection con = null;
        try {
            String sql = "INSERT INTO `telefonos_de_clientes`(`idTelefonos_de_Clientes`, `Clientes_idCliente`, `Telefonos_idTelefono`) "
                    + "VALUES (0,?,?)";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCli);
            ps.setInt(2, idTel);

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

            String sql = "SELECT COUNT(Trabajadores_idTrabajador) FROM telefonos_de_trabajadores WHERE Trabajadores_idTrabajador = ?";
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

            String sql = "SELECT COUNT(Clientes_idCliente) FROM telefonos_de_clientes WHERE Clientes_idCliente = ?";
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
    
    public ArrayList<Telefono> listadoTelefonoAñadir(int[] listado, boolean igualdad, int id) {

        Connection con = getConection();
        ArrayList<Telefono> lista = new ArrayList<>();
        Telefono obj;

        try {
            //idCorreo	correo	contraseña 
            con = getConection();
            String sql = "SELECT * FROM `telefonos` ";

            //si igualdad es true: se tienen que buscar las diferencias de ID
            //si iigualdad es false: se deben buscar las diferencias
            if (igualdad) {
                if (listado.length != 0) {
                    for (int i = 0; i < listado.length; i++) {
                        if (i == 0) {
                            sql += "WHERE idTelefono != " + listado[i] + " ";
                        } else {
                            sql += "AND idTelefono != " + listado[i] + " ";
                        }

                    }
                    System.out.println("SQL TRUE: " + sql); 
                }
            } else {
                sql = "SELECT tel.idTelefono, tel.numero_telefono "
                        + "FROM telefonos_de_trabajadores telTrab "
                        + "INNER JOIN telefonos tel ON tel.idTelefono = telTrab.Telefonos_idTelefono "
                        + "INNER JOIN trabajadores trab ON trab.idTrabajador = telTrab.Trabajadores_idTrabajador "
                        + "WHERE Trabajadores_idTrabajador = "+id+"";
                System.out.println("SQL FALSE: " + sql);
                
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Telefono();
                obj.setId(rs.getInt("idTelefono"));
                obj.setNumero(rs.getString("numero_telefono"));  
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
    
    public ArrayList<Telefono> listadoTelefonoAñadirCliente(int[] listado, boolean igualdad, int id) {

        Connection con = getConection();
        ArrayList<Telefono> lista = new ArrayList<>();
        Telefono obj;

        try {
            //idCorreo	correo	contraseña 
            con = getConection();
            String sql = "SELECT * FROM telefonos ";

            //si igualdad es true: se tienen que buscar las diferencias de ID
            //si iigualdad es false: se deben buscar las diferencias
            if (igualdad) {
                if (listado.length != 0) {
                    for (int i = 0; i < listado.length; i++) {
                        if (i == 0) {
                            sql += "WHERE idTelefono != " + listado[i] + " ";
                        } else {
                            sql += "AND idTelefono != " + listado[i] + " ";
                        }

                    }
                    System.out.println("SQL TRUE: " + sql); 
                }
            } else {
                sql = "SELECT tel.idTelefono, tel.numero_telefono FROM telefonos_de_clientes tc INNER JOIN telefonos tel ON tel.idTelefono = tc.Telefonos_idTelefono INNER JOIN clientes cli ON cli.idCliente = tc.Clientes_idCliente WHERE Clientes_idCliente = "+id+" ";
                System.out.println("SQL FALSE: " + sql);
                
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Telefono();
                obj.setId(rs.getInt("idTelefono"));
                obj.setNumero(rs.getString("numero_telefono"));  
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
    
    public boolean VerificarTel(Telefono obj) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT * FROM `telefonos` WHERE idTelefono = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getId());
            rs = ps.executeQuery();

            if (rs.next()) {
                obj.setId(rs.getInt("idTelefono"));
                obj.setNumero(rs.getString("numero_telefono")); 
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("error CATCH VerificarTel:" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally VerificarTel:" + e);
            }
        }
        return false;
    }

    public int[] idTelefonoSinAñadir(int idTel, int cantidadRegistros) {
        Connection con = getConection();
        int listadoIds[] = new int[cantidadRegistros];

        try {

            String sql = "SELECT Telefonos_idTelefono FROM telefonos_de_trabajadores WHERE Trabajadores_idTrabajador = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idTel);

            rs = ps.executeQuery();
            int cont = 0;
            while (rs.next()) {

                listadoIds[cont] = rs.getInt("Telefonos_idTelefono");

                cont++;
            }

        } catch (Exception e) {
            System.err.println("error: CATCH idTelefonoSinAñadir: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error: Finally idTelefonoSinAñadir" + e);
            }
        }

        return listadoIds;
    }

    public int[] idTelefonoSinAñadirCliente(int idTel, int cantidadRegistros) {
        Connection con = getConection();
        int listadoIds[] = new int[cantidadRegistros];

        try {

            String sql = "SELECT Clientes_idCliente FROM telefonos_de_clientes WHERE Clientes_idCliente = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idTel);

            rs = ps.executeQuery();
            int cont = 0;
            while (rs.next()) {

                listadoIds[cont] = rs.getInt("Telefonos_idTelefono");

                cont++;
            }

        } catch (Exception e) {
            System.err.println("error: CATCH idTelefonoSinAñadirCliente: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error: Finally idTelefonoSinAñadirCliente" + e);
            }
        }

        return listadoIds;
    }
    
    public boolean delete(int idTra, int idtel) {
        Connection con = null;
        try {
            String sql = "DELETE FROM `telefonos_de_trabajadores` WHERE Telefonos_idTelefono = "+idtel+" AND Trabajadores_idTrabajador = "+idTra+"";
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
    
    public boolean deleteCliente(int idCli, int idtel) {
        Connection con = null;
        try {
            String sql = "DELETE FROM `telefonos_de_clientes` WHERE Clientes_idCliente = "+idCli+" AND Telefonos_idTelefono = "+idtel+"";
            con = getConection();
            ps = con.prepareStatement(sql); 

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("error CATCH deleteCliente:" + e);
            return false;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally deleteCliente:" + e);
            }
        }
    }
    
}

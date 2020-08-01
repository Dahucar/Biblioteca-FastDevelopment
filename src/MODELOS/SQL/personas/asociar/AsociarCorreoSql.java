/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.personas.asociar;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.personas.Trabajador;
import MODELOS.MODELOCLASES.personas.datos.Correo;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class AsociarCorreoSql extends ConecctSql{
    private PreparedStatement ps;
    private ResultSet rs;
    
    public boolean insert(int idTrab, int idCorr) {
        Connection con = null;
        try {
            String sql = "INSERT INTO `correos_de_trabajadores`(`idCorreos_de_Trabajadores`, `Correos_idCorreo`, `Trabajadores_idTrabajador`) "
                    + "VALUES (0,?,?)";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCorr);
            ps.setInt(2, idTrab);

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
    
    public boolean insertCliente(int idCli, int idCorr) {
        Connection con = null;
        try {
            String sql = "INSERT INTO `correos_de_clientes`(`idCorreos_de_Clientes`, `Correos_idCorreo`, `Clientes_idCliente`) "
                    + "VALUES (0,?,?)";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCorr);
            ps.setInt(2, idCli);

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

            String sql = "SELECT COUNT(Trabajadores_idTrabajador) FROM correos_de_trabajadores WHERE Trabajadores_idTrabajador = ?";
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

            String sql = "SELECT COUNT(Clientes_idCliente) FROM correos_de_clientes WHERE Clientes_idCliente = ?";
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
    
    public ArrayList<Correo> listadoCorreosAñadir(int[] listado, boolean igualdad, int id) {

        Connection con = getConection();
        ArrayList<Correo> lista = new ArrayList<>();
        Correo obj;

        try {
            //idCorreo	correo	contraseña 
            con = getConection();
            String sql = "SELECT * FROM correos ";

            //si igualdad es true: se tienen que buscar las diferencias de ID
            //si iigualdad es false: se deben buscar las diferencias
            if (igualdad) {
                if (listado.length != 0) {
                    for (int i = 0; i < listado.length; i++) {
                        if (i == 0) {
                            sql += "WHERE idCorreo != " + listado[i] + " ";
                        } else {
                            sql += "AND idCorreo != " + listado[i] + " ";
                        }

                    }
                    System.out.println("SQL TRUE: " + sql); 
                }
            } else {
                sql = "SELECT corr.idCorreo, corr.correo, corr.contraseña "
                        + "FROM correos_de_trabajadores corTrab "
                        + "INNER JOIN correos corr ON corTrab.Correos_idCorreo = corr.idCorreo "
                        + "INNER JOIN trabajadores trab ON corTrab.Trabajadores_idTrabajador = trab.idTrabajador "
                        + "where Trabajadores_idTrabajador = "+id+"";
                System.out.println("SQL FALSE: " + sql);
                
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Correo();
                obj.setId(rs.getInt("idCorreo"));
                obj.setCorreo(rs.getString("correo"));
                obj.setContrasenna(rs.getString("contraseña")); 

                lista.add(obj);

            }

        } catch (Exception e) {
            System.err.println("error: CATCH listadoCorreosAñadir: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error: Finally listadoCorreosAñadir" + e);
            }
        }

        return lista;
    }
    
    public ArrayList<Correo> listadoCorreosAñadirCliente(int[] listado, boolean igualdad, int id) {

        Connection con = getConection();
        ArrayList<Correo> lista = new ArrayList<>();
        Correo obj;

        try {
            //idCorreo	correo	contraseña 
            con = getConection();
            String sql = "SELECT * FROM correos ";

            //si igualdad es true: se tienen que buscar las diferencias de ID
            //si iigualdad es false: se deben buscar las diferencias
            if (igualdad) {
                if (listado.length != 0) {
                    for (int i = 0; i < listado.length; i++) {
                        if (i == 0) {
                            sql += "WHERE idCorreo != " + listado[i] + " ";
                        } else {
                            sql += "AND idCorreo != " + listado[i] + " ";
                        }

                    }
                    System.out.println("SQL TRUE: " + sql); 
                }
            } else {
                sql = "SELECT corr.idCorreo, corr.correo, corr.contraseña FROM correos_de_clientes corCli INNER JOIN correos corr ON corCli.Correos_idCorreo = corr.idCorreo INNER JOIN clientes cli ON corCli.Clientes_idCliente = cli.idCliente where Clientes_idCliente =  "+id+"";
                System.out.println("SQL FALSE: " + sql);
                
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                //idCorreo	correo	contraseña 
                obj = new Correo();
                obj.setId(rs.getInt("idCorreo"));
                obj.setCorreo(rs.getString("correo"));
                obj.setContrasenna(rs.getString("contraseña")); 

                lista.add(obj);

            }

        } catch (Exception e) {
            System.err.println("error: CATCH listadoCorreosAñadirCliente: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error: Finally listadoCorreosAñadirCliente" + e);
            }
        }

        return lista;
    }
    
    public boolean VerificarCorreo(Correo obj) {
        Connection con = null;
        boolean est = true;
        try {

            String sql = "SELECT * FROM `correos` WHERE idCorreo = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getId());
            rs = ps.executeQuery();

            if (rs.next()) {
                obj.setId(rs.getInt("idCorreo"));
                obj.setCorreo(rs.getString("correo"));
                obj.setContrasenna(rs.getString("contraseña")); 
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("error CATCH VerificarCorreo:" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally VerificarCorreo:" + e);
            }
        }
        return false;
    }

    public int[] idCorreosSinAñadir(int idTrabajar, int cantidadRegistros) {
        Connection con = getConection();
        int listadoIds[] = new int[cantidadRegistros];

        try {

            String sql = "SELECT Correos_idCorreo FROM correos_de_trabajadores WHERE Trabajadores_idTrabajador = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idTrabajar);

            rs = ps.executeQuery();
            int cont = 0;
            while (rs.next()) {

                listadoIds[cont] = rs.getInt("Correos_idCorreo");

                cont++;
            }

        } catch (Exception e) {
            System.err.println("error: CATCH idCorreosSinAñadir: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error: Finally idCorreosSinAñadir" + e);
            }
        }

        return listadoIds;
    }

    public int[] idCorreosSinAñadirCliente(int idCli, int cantidadRegistros) {
        Connection con = getConection();
        int listadoIds[] = new int[cantidadRegistros];

        try {

            String sql = "SELECT Correos_idCorreo FROM correos_de_clientes WHERE Clientes_idCliente = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCli);

            rs = ps.executeQuery();
            int cont = 0;
            while (rs.next()) {

                listadoIds[cont] = rs.getInt("Correos_idCorreo");

                cont++;
            }

        } catch (Exception e) {
            System.err.println("error: CATCH idCorreosSinAñadirCliente: " + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error: Finally idCorreosSinAñadirCliente: " + e);
            }
        }

        return listadoIds;
    }
    
    public boolean delete(int idTra, int idcorr) {
        Connection con = null;
        try {
            String sql = "DELETE FROM `correos_de_trabajadores` WHERE Trabajadores_idTrabajador = "+idTra+" AND Correos_idCorreo = "+idcorr+"";
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
    
    public boolean deleteCliente(int idClie, int idcorr) {
        Connection con = null;
        try {
            String sql = "DELETE FROM `correos_de_clientes` WHERE Clientes_idCliente = "+idClie+" and Correos_idCorreo = "+idcorr+"";
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

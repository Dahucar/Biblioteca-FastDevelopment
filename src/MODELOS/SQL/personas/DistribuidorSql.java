/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.personas;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.personas.Distribuidor;
import MODELOS.MODELOCLASES.personas.datos.Direccion;
import MODELOS.MODELOCLASES.personas.datos.Telefono;
import com.mysql.jdbc.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class DistribuidorSql extends ConecctSql{
    private PreparedStatement ps;
    private ResultSet rs;
    
    public ArrayList<Distribuidor> listar() {
        Connection con = getConection();
        ArrayList<Distribuidor> lista = new ArrayList<>();
        Distribuidor obj; 

        try {
            //idDistribuidore	rut	nombre_empresa	anno_sociedad	Telefonos_idTelefono	
            //Direcciones_idDireccion	idTelefono	numero_telefono	idDireccion	direccion	
            String sql = "SELECT * FROM distribuidores dist "
                    + "INNER JOIN telefonos tel ON tel.idTelefono = dist.Telefonos_idTelefono "
                    + "INNER JOIN direcciones direcc ON direcc.idDireccion = dist.Direcciones_idDireccion";
            con = getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Distribuidor();
                obj.setId(rs.getInt("idDistribuidore"));
                obj.setRut(rs.getString("rut"));
                obj.setNombre_empresa(rs.getString("nombre_empresa"));
                obj.setAnno_asociadad(rs.getDate("anno_sociedad"));
                obj.setTelefono(new Telefono(rs.getInt("idTelefono"), rs.getString("numero_telefono")));
                obj.setDireccion(new Direccion(rs.getInt("idDireccion"), rs.getString("direccion"))); 

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

    public boolean SELECT(Distribuidor modelo) {
        com.mysql.jdbc.Connection con = null;

        try {

            String sql = "SELECT * FROM distribuidores dist INNER JOIN telefonos tel ON tel.idTelefono = dist.Telefonos_idTelefono INNER JOIN direcciones direc ON direc.idDireccion = dist.Direcciones_idDireccion WHERE dist.idDistribuidore = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, modelo.getId());
            rs = ps.executeQuery();
            //idDistribuidore	rut	nombre_empresa	anno_sociedad	Telefonos_idTelefono	Direcciones_idDireccion	idTelefono	numero_telefono	idDireccion	direccion
            if (rs.next()) { 
                modelo.setId(rs.getInt("idDistribuidore"));
                modelo.setRut(rs.getString("rut"));
                modelo.setNombre_empresa(rs.getString("nombre_empresa"));
                modelo.setAnno_asociadad(rs.getDate("anno_sociedad"));
                modelo.setTelefono(new Telefono(rs.getInt("idTelefono"), rs.getString("numero_telefono")));
                modelo.setDireccion(new Direccion(rs.getInt("idDireccion"), rs.getString("direccion"))); 
                
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

    public int VerificarRut(Distribuidor modelo) {
        Connection con = null;

        try { 
            String sql = "SELECT COUNT(rut) FROM distribuidores WHERE rut = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, modelo.getRut());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("COUNT(rut)");  
            }

            return -1; 
        } catch (Exception e) {
            System.err.println("error CATCH VerificarRut:" + e);
            return -1;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally  VerificarRut:" + e);
            }
        }
    }

    public boolean INSERT(Distribuidor obj) {
        Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `distribuidores`(`idDistribuidore`, `rut`, `nombre_empresa`, `anno_sociedad`, `Telefonos_idTelefono`, `Direcciones_idDireccion`) "
                    + "VALUES (0,?,?,?,?,?)";
            ps = con.prepareStatement(sql); 
            ps.setString(1, obj.getRut());
            ps.setString(2, obj.getNombre_empresa());
            ps.setDate(3, (Date) obj.getAnno_asociadad());
            ps.setInt(4, obj.getTelefono().getId());
            ps.setInt(5, obj.getDireccion().getId());

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

    public boolean REMOVE(Distribuidor obj) {
        Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `distribuidores` WHERE idDistribuidore = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getId()); 
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

    public boolean UPDATE(Distribuidor obj) {
        Connection con = null;
        try {
            String sql = "UPDATE `distribuidores` SET "
                    + "`rut` = ?,"
                    + "`nombre_empresa` = ?,"
                    + "`anno_sociedad` = ?,"
                    + "`Telefonos_idTelefono` = ?,"
                    + "`Direcciones_idDireccion` = ? "
                    + "WHERE `idDistribuidore` = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getRut());
            ps.setString(2, obj.getNombre_empresa());
            ps.setDate(3, (Date) obj.getAnno_asociadad());   
            ps.setInt(4, obj.getTelefono().getId());
            ps.setInt(5, obj.getDireccion().getId()); 
            ps.setInt(6, obj.getId());

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

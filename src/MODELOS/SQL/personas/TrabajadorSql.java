
package MODELOS.SQL.personas;

import MODELOS.CONFIG_SQL.ConecctSql; 
import MODELOS.MODELOCLASES.personas.Trabajador;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *  
 * @author Daniel Huenul
 */
public class TrabajadorSql extends ConecctSql{
    
    private PreparedStatement ps;
    private ResultSet rs;
    
    public boolean INSERT(Trabajador modelo) {
        Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `trabajadores`(`idTrabajador`, `rutTrabajador`, `nombre`, `apellido_p`, `apellido_m`) "
                    + "VALUES (0,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, modelo.getRut());
            ps.setString(2, modelo.getNombre());
            ps.setString(3, modelo.getApellido_p());
            ps.setString(4, modelo.getApellido_m());

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
    
    public boolean verificar(String rut) {
        Connection con = null;
        try {

            String sql = "SELECT * FROM trabajadores WHERE rutTrabajador = '"+ rut +"'";
            con = getConection(); 
            ps = con.prepareStatement(sql); 
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

    public ArrayList<Trabajador> listar() {
        Connection con = getConection();
        ArrayList<Trabajador> lista = new ArrayList<>();
        Trabajador obj;

        try {

            String sql = "SELECT * FROM trabajadores";
            con = getConection();
            //idTrabajador`, `rutTrabajador`, `nombre`, `apellido_p`, `apellido_m`
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Trabajador();
                obj.setId(rs.getInt("idTrabajador"));   
                obj.setRut(rs.getString("rutTrabajador"));
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

    public boolean REMOVE(Trabajador modelo) {
        Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `trabajadores` WHERE rutTrabajador = '"+ modelo.getRut() +"'";
            con = getConection();
            ps = con.prepareStatement(sql); 
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

    public boolean SELECT(Trabajador modelo) {
        Connection con = null;

        try { 
            String sql = "SELECT * FROM trabajadores WHERE rutTrabajador = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, modelo.getRut());
            rs = ps.executeQuery();

            if (rs.next()) { 
                modelo.setId(rs.getInt("idTrabajador")); 
                modelo.setRut(rs.getString("rutTrabajador"));
                modelo.setNombre(rs.getString("nombre"));
                modelo.setApellido_p(rs.getString("apellido_p"));
                modelo.setApellido_m(rs.getString("apellido_m")); 
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

    public boolean UPDATE(Trabajador modelo) {
        Connection con = null;
        try {
            String sql = "UPDATE `trabajadores` SET " 
                    + "`rutTrabajador`= '"+ modelo.getRut() +"',"
                    + "`nombre`= '"+ modelo.getNombre()+"',"
                    + "`apellido_p`= '"+ modelo.getApellido_p()+"',"
                    + "`apellido_m`= '"+ modelo.getApellido_m()+"' "
                    + "WHERE idTrabajador = "+ modelo.getId()+"";
            con = getConection(); 
            ps = con.prepareStatement(sql);
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

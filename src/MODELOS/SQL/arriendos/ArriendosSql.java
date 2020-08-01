/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.arriendos;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.arriendos.Arriendos;
import MODELOS.MODELOCLASES.libros.Editorial;
import MODELOS.MODELOCLASES.libros.Estado;
import MODELOS.MODELOCLASES.libros.Libro;
import MODELOS.MODELOCLASES.personas.Cliente;
import MODELOS.MODELOCLASES.personas.Trabajador;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class ArriendosSql extends ConecctSql {

    private PreparedStatement ps;
    private ResultSet rs;
    
    public ArrayList<Arriendos> listar() {
        Connection con = getConection();
        ArrayList<Arriendos> lista = new ArrayList<>();
        Arriendos obj; 

        try {

            String sql = "SELECT ar.idArriendo AS 'IDARRI', ar.codigoArriendo AS 'CODARRI', ar.costo_total AS 'COSTALARRI', ar.costo_arriendo AS 'COSTARRI', ar.fecha_arriendo AS 'FECHARRI', ar.fecha_devolucion AS 'FECHDEVOLARRI', ar.fecha_entrega AS 'FECHENTREARRI', ar.dias_retraso AS 'DIASRETRAARRI', ar.multa AS 'MULTARRI', cli.idCliente AS 'IDCLI', cli.rutCliente AS 'RUTCLI',cli.nombre AS 'NOMCLI', cli.apellido_p AS 'APEPCLI', cli.apellido_m AS 'APEMCLI', tra.idTrabajador AS 'IDTRA', tra.rutTrabajador AS 'RUTTRA', tra.nombre AS 'NOMTRA', tra.apellido_p AS 'APEPTRA', tra.apellido_m AS 'APEMTRA' FROM arriendos ar INNER JOIN clientes cli ON cli.idCliente = ar.Clientes_idCliente INNER JOIN trabajadores tra ON tra.idTrabajador = ar.Trabajadores_idTrabajador";
            con = getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                obj = new Arriendos(); 
                //IDARRI	CODARRI	COSTALARRI	COSTARRI	FECHARRI	FECHDEVOLARRI	
                //FECHENTREARRI	DIASRETRAARRI	MULTARRI	IDCLI	RUTCLI	NOMCLI	APEPCLI	APEMCLI	
                //IDTRA	RUTTRA	NOMTRA	APEPTRA	APEMTRA
                obj.setId(rs.getInt("IDARRI"));
                obj.setCodigo(rs.getString("CODARRI"));
                obj.setCosto_total(rs.getInt("COSTALARRI"));
                obj.setCosto_arriendo(rs.getInt("COSTARRI"));
                obj.setFecha_arriendo(rs.getDate("FECHARRI"));
                obj.setFecha_devolucion(rs.getDate("FECHDEVOLARRI"));
                obj.setFecha_entrega(rs.getDate("FECHENTREARRI"));
                obj.setDias_atrazo(rs.getInt("DIASRETRAARRI"));
                obj.setMulta(rs.getInt("MULTARRI"));
                obj.setCliente(new Cliente(
                        null, 
                        rs.getInt("IDCLI"), 
                        rs.getString("RUTCLI"), 
                        rs.getString("NOMCLI"), 
                        rs.getString("APEPCLI"), 
                        rs.getString("APEMCLI")));
                obj.setTrabjador(new Trabajador(
                        rs.getInt("IDTRA"), 
                        rs.getString("RUTTRA"), 
                        rs.getString("NOMTRA"), 
                        rs.getString("APEPTRA"), 
                        rs.getString("APEMTRA")));
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

    public int Verificar(Arriendos modelo) {
        com.mysql.jdbc.Connection con = null;

        try {

            String sql = "SELECT COUNT(codigoArriendo) FROM arriendos WHERE codigoArriendo = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, modelo.getCodigo());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("COUNT(codigoArriendo)");  
            }

            return -1;

        } catch (Exception e) {
            System.err.println("error CATCH Verificar :" + e);
            return -1;
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("error finally Verificar :" + e);
            }
        }
    }

    public boolean INSERT(Arriendos obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `arriendos`(`idArriendo`, `codigoArriendo`, `costo_total`, `costo_arriendo`, `fecha_arriendo`, `fecha_devolucion`, `fecha_entrega`, `dias_retraso`, `multa`, `Trabajadores_idTrabajador`, `Clientes_idCliente`) "
                    + "VALUES (0,?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getCodigo());
            ps.setFloat(2, obj.getCosto_total());
            ps.setFloat(3, obj.getCosto_arriendo());
            ps.setDate(4, (Date) obj.getFecha_arriendo());
            ps.setDate(5, (Date) obj.getFecha_devolucion());
            ps.setDate(6, (Date) obj.getFecha_entrega());
            ps.setInt(7, obj.getDias_atrazo());
            ps.setFloat(8, obj.getMulta());
            ps.setInt(9, obj.getTrabjador().getId());
            ps.setInt(10, obj.getCliente().getId());

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

    public boolean SELECT(Arriendos obj) {
        com.mysql.jdbc.Connection con = null;

        try {

            String sql = "SELECT ar.idArriendo AS 'IDARRI', ar.codigoArriendo AS 'CODARRI', ar.costo_total AS 'COSTALARRI', ar.costo_arriendo AS 'COSTARRI', ar.fecha_arriendo AS 'FECHARRI', ar.fecha_devolucion AS 'FECHDEVOLARRI', ar.fecha_entrega AS 'FECHENTREARRI', ar.dias_retraso AS 'DIASRETRAARRI', ar.multa AS 'MULTARRI', cli.idCliente AS 'IDCLI', cli.rutCliente AS 'RUTCLI',cli.nombre AS 'NOMCLI', cli.apellido_p AS 'APEPCLI', cli.apellido_m AS 'APEMCLI', tra.idTrabajador AS 'IDTRA', tra.rutTrabajador AS 'RUTTRA', tra.nombre AS 'NOMTRA', tra.apellido_p AS 'APEPTRA', tra.apellido_m AS 'APEMTRA' FROM arriendos ar INNER JOIN clientes cli ON cli.idCliente = ar.Clientes_idCliente INNER JOIN trabajadores tra ON tra.idTrabajador = ar.Trabajadores_idTrabajador WHERE idArriendo = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getId());
            rs = ps.executeQuery();

            if (rs.next()) {
                obj.setId(rs.getInt("IDARRI"));
                obj.setCodigo(rs.getString("CODARRI"));
                obj.setCosto_total(rs.getInt("COSTALARRI"));
                obj.setCosto_arriendo(rs.getInt("COSTARRI"));
                obj.setFecha_arriendo(rs.getDate("FECHARRI"));
                obj.setFecha_devolucion(rs.getDate("FECHDEVOLARRI"));
                obj.setFecha_entrega(rs.getDate("FECHENTREARRI"));
                obj.setDias_atrazo(rs.getInt("DIASRETRAARRI"));
                obj.setMulta(rs.getInt("MULTARRI"));
                obj.setCliente(new Cliente(
                        null, 
                        rs.getInt("IDCLI"), 
                        rs.getString("RUTCLI"), 
                        rs.getString("NOMCLI"), 
                        rs.getString("APEPCLI"), 
                        rs.getString("APEMCLI")));
                obj.setTrabjador(new Trabajador(
                        rs.getInt("IDTRA"), 
                        rs.getString("RUTTRA"), 
                        rs.getString("NOMTRA"), 
                        rs.getString("APEPTRA"), 
                        rs.getString("APEMTRA")));
                
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

    public boolean REMOVE(Arriendos obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `arriendos` WHERE idArriendo = ?";
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

    public boolean UPDATE(Arriendos obj) {
        Connection con = null;
        try {
            String sql = "UPDATE `arriendos` SET "
                    + "`codigoArriendo`=?,"
                    + "`costo_total`=?,"
                    + "`costo_arriendo`=?,"
                    + "`fecha_arriendo`=?,"
                    + "`fecha_devolucion`=?,"
                    + "`fecha_entrega`=?,"
                    + "`dias_retraso`=?,"
                    + "`multa`=?,"
                    + "`Trabajadores_idTrabajador`=?,"
                    + "`Clientes_idCliente`=? "
                    + "WHERE `idArriendo`= ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getCodigo());
            ps.setFloat(2, obj.getCosto_total());
            ps.setFloat(3, obj.getCosto_arriendo()); 
            ps.setDate(4, (Date) obj.getFecha_arriendo());
            ps.setDate(5, (Date) obj.getFecha_devolucion());
            ps.setDate(6, (Date) obj.getFecha_entrega()); 
            ps.setInt(7, obj.getDias_atrazo());
            ps.setFloat(8, obj.getMulta()); 
            ps.setInt(9, obj.getTrabjador().getId());
            ps.setInt(10, obj.getCliente().getId());
            ps.setInt(11, obj.getId());

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

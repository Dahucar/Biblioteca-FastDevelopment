/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.SQL.ventas;

import MODELOS.CONFIG_SQL.ConecctSql;
import MODELOS.MODELOCLASES.personas.Cliente;
import MODELOS.MODELOCLASES.personas.Trabajador;
import MODELOS.MODELOCLASES.ventas.Boletas;
import MODELOS.MODELOCLASES.ventas.MetodoPago;
import MODELOS.MODELOCLASES.ventas.Ventas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Daniel Huenul
 */
public class VentasSql extends ConecctSql {

    private PreparedStatement ps;
    private ResultSet rs;

    public ArrayList<Ventas> listar() {
        Connection con = getConection();
        ArrayList<Ventas> lista = new ArrayList<>();
        Ventas obj; 

        try {

            String sql = "SELECT * FROM ventass vent "
                    + "INNER JOIN clientes cli ON cli.idCliente = vent.Clientes_idCliente "
                    + "INNER JOIN trabajadores tra ON tra.idTrabajador = vent.Trabajadores_idTrabajador "
                    + "INNER JOIN boletas bol ON bol.idBoleta = vent.Boletas_idBoleta "
                    + "INNER JOIN metodospagos mt ON mt.idMetodoPago = bol.MetodosPagos_idMetodoPago";
            con = getConection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            //idVenta	codigoVenta 
            //idCliente | rutCliente | nombre | apellido_p | apellido_m | fecha_contrato	
            //idTrabajador | rutTrabajador | nombre | apellido_p | apellido_m
            //idBoleta | folio | precio_neto | costo_iva | precio_iva | fecha_venta | 
            //MetodosPagos_idMetodoPago	idMetodoPago	metodo	

            while (rs.next()) {
                obj = new Ventas();  
                obj.setId(rs.getInt("idVenta"));
                obj.setCodigo_venta(String.valueOf(rs.getInt("codigoVenta")));
                obj.setCliente(new Cliente(
                        rs.getDate("fecha_contrato"), 
                        rs.getInt("idCliente"), 
                        rs.getString("rutCliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido_p"),
                        rs.getString("apellido_m")));
                obj.setTrabajador(new Trabajador(
                        rs.getInt("idTrabajador"), 
                        rs.getString("rutTrabajador"), 
                        rs.getString("nombre"),
                        rs.getString("apellido_p"),
                        rs.getString("apellido_m")));
                obj.setBoleta(new Boletas(
                        rs.getInt("idBoleta"), 
                        rs.getInt("folio"), 
                        rs.getFloat("precio_neto"), 
                        rs.getFloat("costo_iva"),
                        rs.getFloat("precio_iva"),
                        rs.getString("fecha_venta"), 
                        new MetodoPago(
                            rs.getInt("idMetodoPago"), 
                        rs.getString("metodo")))); 

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

    public boolean SELECT(Ventas obj) {
        Connection con = null;

        try {

            String sql = "SELECT vent.idVenta AS 'IDVENT', vent.codigoVenta AS 'CODVENT', cli.idCliente AS 'IDCLI', "
                    + "cli.rutCliente AS 'RUTCLI', cli.nombre AS 'NOMCLI', cli.apellido_p AS 'APEPCLI', cli.apellido_m AS 'APEMCLI', "
                    + "cli.fecha_contrato AS 'FECHCONTCLI', tra.idTrabajador AS 'IDTRA', tra.rutTrabajador AS 'RUTTRA', "
                    + "tra.nombre AS 'NOMTRA', tra.apellido_p AS 'APEPTRA', tra.apellido_m AS 'APEMTRA', bol.idBoleta AS 'IDBOL', "
                    + "bol.folio AS 'FOLBOL', bol.precio_neto AS 'PRECNETOBOL', bol.costo_iva AS 'COSTIVABOL', "
                    + "bol.precio_neto AS 'PRECIVABOL', bol.fecha_venta AS 'FECHBOL', mt.idMetodoPago AS 'ODMETPAGO', "
                    + "mt.metodo AS 'METPAGO' "
                    + "FROM ventass vent "
                    + "INNER JOIN clientes cli ON cli.idCliente = vent.Clientes_idCliente "
                    + "INNER JOIN trabajadores tra ON tra.idTrabajador = vent.Trabajadores_idTrabajador "
                    + "INNER JOIN boletas bol ON bol.idBoleta = vent.Boletas_idBoleta "
                    + "INNER JOIN metodospagos mt ON mt.idMetodoPago = bol.MetodosPagos_idMetodoPago "
                    + "WHERE idVenta = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, obj.getId());
            rs = ps.executeQuery();

            if (rs.next()) {
                //IDVENT
                //CODVENT
                //IDCLI
                //RUTCLI
                //NOMCLI
                //APEPCLI
                //APEMCLI
                //FECHCONTCLI
                //IDTRA
                //RUTTRA
                //NOMTRA
                //APEPTRA
                //APEMTRA
                //IDBOL
                //FOLBOL
                //PRECNETOBOL
                //COSTIVABOL
                //PRECIVABOL
                //FECHBOL
                //ODMETPAGO
                //METPAGO

                obj.setId(rs.getInt("IDVENT"));
                obj.setCodigo_venta(String.valueOf(rs.getInt("CODVENT")));
                obj.setCliente(new Cliente(
                        rs.getDate("FECHCONTCLI"), 
                        rs.getInt("IDCLI"), 
                        rs.getString("RUTCLI"),
                        rs.getString("NOMCLI"),
                        rs.getString("APEPCLI"),
                        rs.getString("APEMCLI")));
                obj.setTrabajador(new Trabajador(
                        rs.getInt("IDTRA"), 
                        rs.getString("RUTTRA"), 
                        rs.getString("NOMTRA"),
                        rs.getString("APEPTRA"),
                        rs.getString("APEMTRA")));
                obj.setBoleta(new Boletas(
                        rs.getInt("IDBOL"), 
                        rs.getInt("FOLBOL"), 
                        rs.getFloat("PRECNETOBOL"), 
                        rs.getFloat("COSTIVABOL"),
                        rs.getFloat("PRECIVABOL"),
                        rs.getString("FECHBOL"), 
                        new MetodoPago(
                            rs.getInt("ODMETPAGO"), 
                        rs.getString("METPAGO"))));
                
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

    public int Verificar(Ventas modelo) {
        Connection con = null;

        try {

            String sql = "SELECT COUNT(codigoVenta) FROM ventass WHERE codigoVenta = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, modelo.getCodigo_venta());
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("COUNT(codigoVenta)");  
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

    public boolean INSERT(Ventas obj) {
        Connection con = null;
        try {
            con = getConection();
            String sql = "INSERT INTO `ventass`(`idVenta`, `codigoVenta`, `Clientes_idCliente`, `Trabajadores_idTrabajador`, `Boletas_idBoleta`) "
                    + "VALUES (0,?,?,?,?)";
            ps = con.prepareStatement(sql); 
            ps.setString(1, obj.getCodigo_venta());
            ps.setInt(2, obj.getCliente().getId());
            ps.setInt(3, obj.getTrabajador().getId()); 
            ps.setInt(4, obj.getBoleta().getId()); 

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

    public boolean REMOVE(Ventas obj) {
        com.mysql.jdbc.Connection con = null;
        try {
            //insetamos todos los reguistros a la tabla de autores obteniendo los datos de el objeto autor que llega como parametro
            String sql = "DELETE FROM `ventass` WHERE idVenta = ?";
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

    public boolean UPDATE(Ventas obj) {
        Connection con = null;
        try {
            String sql = "UPDATE `ventass` SET  codigoVenta = ?, Clientes_idCliente = ?, Trabajadores_idTrabajador = ?, Boletas_idBoleta = ? WHERE idVenta = ?";
            con = getConection();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getCodigo_venta());
            ps.setInt(2, obj.getCliente().getId());
            ps.setInt(3, obj.getTrabajador().getId()); 
            ps.setInt(4, obj.getBoleta().getId());  
            ps.setInt(5, obj.getId());

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

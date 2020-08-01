/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELOS.CONFIG_SQL;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel Huenul
 */
public class ConecctSql {
    private static final String URL = "jdbc:mysql://localhost:3308/biblioteca_fastdevelopment?autoReconnect=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    
    public Connection getConection(){
        Connection conexion = null;
       
        try {
            Class.forName(DRIVER);
            conexion = (Connection) DriverManager.getConnection(URL, USER, PASSWORD); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No hay conexión con la base de datos");
        }
        
        return conexion;
    }
}

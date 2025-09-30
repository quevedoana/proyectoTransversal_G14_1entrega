/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Anitabonita
 */
public class Conexion {
    private static String url = "jdbc:mariadb://localhost:3306/sgulp";
    private static String usuario = "root";
    private static String password = "";
    private static Connection conexion = null;

    public Conexion(String url, String usuario, String pass){
        this.url = url;
        this.usuario = usuario;
        this.password = pass;
    }
    
    public static Connection getConexion() {
        if (conexion == null) {
            try {
                Class.forName("org.mariadb.jdbc.Driver");
                conexion = DriverManager.getConnection(url, usuario, password);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Error al cargar el driver: " + ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error de conexión: " + ex.getMessage());
            }
        }
        return conexion;
    }
}

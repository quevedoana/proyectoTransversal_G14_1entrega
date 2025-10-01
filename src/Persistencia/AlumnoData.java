/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Modelo.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Anitabonita
 */
public class AlumnoData {
    private Connection conexion = null;
    
    public AlumnoData(Conexion conex){
        this.conexion=conex.getConexion();
    }

    public AlumnoData() {
    }
    
    
    public void guardarAlumno(Alumno a){ //insert
        String query= "INSERT INTO alumno( DNI, Apellido, Nombre, FechaNacimiento, Estado) VALUES (?,?,?,?,?)";
        try{
            PreparedStatement ps = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, a.getDni());
            ps.setString(2, a.getApellido());
            ps.setString(3, a.getNombre());
            ps.setDate(4, Date.valueOf(a.getFechaNacimiento()));
            ps.setBoolean(5, a.isEstado());
            ps.executeUpdate();
            
            ResultSet rs= ps.getGeneratedKeys();
            if(rs.next()){
                a.setIdAlumno(rs.getInt(1));
            }else{
                JOptionPane.showMessageDialog(null, "no se pudo obtener el id");
                ps.close();
            }
        } catch (SQLException e){
            Logger.getLogger(AlumnoData.class.getName()).log(Level.SEVERE, null, e);
        
    }
     
    }
   /* public Alumno buscarAlumno (int id){ //select 1 alumno
        
    }
    
    public List<Alumno> listarAlumno(){ //select *
        
    }*/
    
    public void actualizarAlumno(Alumno a){
        
    }
    public void borrarAlumno (int id){
        
    }
}

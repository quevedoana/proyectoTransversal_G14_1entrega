/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Modelo.Alumno;
import Modelo.Conexion;
import Modelo.Inscripcion;
import Modelo.Materia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author maria
 */
public class InscripcionData {
    private Connection conexion = null;
    
    public InscripcionData(){
        this.conexion= Conexion.getConexion();
    }
    
    public void guardarInscripcion(Inscripcion i){
    try{
        String sql = "INSERT INTO inscripcion (idAlumno,idMateria,nota) VALUES (?,?,?);";
        PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, i.getAlumno().getIdAlumno());
        ps.setInt(2, i.getMateria().getIdMateria());
        ps.setDouble(3,i.getNota());
        ps.executeUpdate();
        
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()){
            i.setNota(rs.getInt(1));
            JOptionPane.showMessageDialog(null, "Inscripción Registrada");
        
        }
        else{
        JOptionPane.showMessageDialog(null, "No se pudo obtener el id de la inscripción");
        
        }
        ps.close();
        
                
        
    }catch(SQLException e){
         JOptionPane.showMessageDialog(null, "Error al guardar la inscripción" + e.getMessage());
    }
}
    public List<Inscripcion> buscarInscripcionesPorAlumno(int idAlumno){
         String sql = "SELECT * FROM cursada WHERE idAlumno=?; " ;
          List<Inscripcion> inscripciones = new ArrayList();
        try{
       
        PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, idAlumno);
        ResultSet rs = ps.executeQuery();
       
       while(rs.next()){
          Inscripcion in = new Inscripcion();
           
           in.setIdInscripto(rs.getInt("idInscripcion"));
           
           Alumno a = traerAlumno(rs.getInt("idAlumno"));
           in.setAlumno(a);
           
           Materia m = buscarMateria(rs.getInt("idMateria"));
           in.setMateria(m);
           
           in.setNota(rs.getDouble("nota"));
           
           inscripciones.add(in);   
           
       }
       
       ps.close();
        
        }catch(SQLException e){
              JOptionPane.showMessageDialog(null, "Error al acceder a la tabla inscripción" + e.getMessage());
        }
        return inscripciones;
    }
   
    public Alumno traerAlumno(int idAlumno){
        AlumnoData ad = new AlumnoData();
        return ad.buscarAlumnoPorId(idAlumno);
    }
    public Materia buscarMateria(int idMateria){
        MateriaData md = new MateriaData();
        return md.buscarMateriaPorId(idMateria);
    }
    
    public List<Inscripcion> listarInscripciones(){
        String sql = "SELECT * FROM inscripcion WHERE 1";
        List<Inscripcion> inscripciones = new ArrayList();
        try{
           
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                 Inscripcion in = new Inscripcion();
           in.setIdInscripto(rs.getInt("idInscripcion"));
           
          Alumno a = traerAlumno(rs.getInt("idAlumno"));
           in.setAlumno(a);
           
           Materia m = buscarMateria(rs.getInt("idMateria"));
           in.setMateria(m);
           
           in.setNota(rs.getDouble("nota"));
           inscripciones.add(in);
                
            }
            ps.close();
            
            
        }catch(SQLException e){
             JOptionPane.showMessageDialog(null, "Error al acceder a la tabla inscripción" + e.getMessage());
        }
        return inscripciones;
    }
    public void actualizarNota(int idAlumno, int idMateria, double nota){
        String sql = "UPDATE inscripcion SET nota=? WHERE idAlumno=? and idMateria=?";
        try {
            PreparedStatement ps =conexion.prepareStatement(sql);
            
            ps.setDouble(1, nota);
            ps.setInt(1, idAlumno);
            ps.setInt(3, idMateria);
            int fila=ps.executeUpdate();
            if(fila>0){
                 JOptionPane.showMessageDialog(null, "Nota actualizada");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al acceder a la tabla inscripción" + e.getMessage());
            
        }
        
    }
    public void borrarInscripcion(int idAlumno, int idMateria){
        String sql = "DELETE FROM inscripcion WHERE idAlumno=? and idMateria=?";
        try{
             PreparedStatement ps = conexion.prepareStatement(sql);
             ps.setInt(1, idAlumno);
             ps.setInt(2, idMateria);
            int fila= ps.executeUpdate();
            if(fila>0){
                JOptionPane.showMessageDialog(null, "Inscripción borrada");
            }
             
             ps.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al acceder a la tabla inscripción" + e.getMessage());        }
    }
    public List<Materia> obtenerMateriasCursadas(int idAlumno){
         List<Materia> materias = new ArrayList();
         String sql = "SELECT inscripcion.idMateria, nombre, anio FROM inscripcion,"
                 + "materia WHERE inscripcion.idMateria = materia.idMateria " +
                 " AND inscripcion.idAlumno=?;";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idAlumno);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Materia materia = new Materia();
                materia.setIdMateria(rs.getInt("idMateria"));
                materia.setNombre(rs.getString("nombre"));
                materia.setAnio(rs.getInt("anio"));
                materias.add(materia);
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al acceder a la tabla inscripción" + e.getMessage());
        }
return materias;
    }
     public List<Materia> obtenerMateriasNoCursadas(int idAlumno){
          List<Materia> materias = new ArrayList();
          String sql = "SELECT * FROM materia WHERE estado=1 AND idMateria NOT IN "
                  + "(SELECT idMateria FROM inscripcion WHERE idAlumno=?)";
           try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idAlumno);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Materia materia = new Materia();
                materia.setIdMateria(rs.getInt("idMateria"));
                materia.setNombre(rs.getString("nombre"));
                materia.setAnio(rs.getInt("anio"));
                materias.add(materia);
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al acceder a la tabla inscripción" + e.getMessage());
        }
return materias;
          
     }
     public List<Alumno> obtenerAlumnoXMateria(int idMateria){
         List<Alumno> alumnos = new ArrayList();
          String sql = "SELECT a.idAlumno, Nombre, Apellido,FechaNacimiento, Estado "
                    +"FROM inscripcion i, alumno a WHERE i.idAlumno = a.idAlumno AND idMateria=? AND a.Estado = 1 ";
         try {
           
           
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idMateria);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Alumno alumno = new Alumno();
                alumno.setIdAlumno(rs.getInt("idAlumno"));
                alumno.setDni(rs.getInt("DNI"));
                alumno.setApellido(rs.getString("Apellido"));
                alumno.setNombre(rs.getString("Nombre"));
                alumno.setFechaNacimiento(LocalDate.parse(rs.getString("FechaNacimiento")));
                alumno.setEstado(rs.getBoolean("Estado"));
                alumnos.add(alumno);
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al acceder a la tabla inscripción" + e.getMessage());
        }
         return alumnos;
     
     }
}

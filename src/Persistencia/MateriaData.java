/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Modelo.Conexion;
import Modelo.Materia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author maria
 */
public class MateriaData {

    //insertar, actualizar, buscar, baja/altaLogica, y borrar
    private Connection conexion = null;

    public MateriaData() {
        conexion = Conexion.getConexion();;
    }

    

    public void guardarMateria(Materia m) {
        String query = "INSERT INTO materia(nombre, año, Estado) VALUES (?,?,?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, m.getNombre());
            ps.setInt(2, m.getAnio());
            ps.setBoolean(3, m.isEstado());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                m.setIdMateria(rs.getInt(1));
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo obtener el id de la materia");
                ps.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar la materia" + e.getMessage());
        }
    }

    public Materia buscarMateria(String nombre) {

        String sql = "SELECT * FROM materia WHERE nombre LIKE ?";
        Materia mate = null;
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, nombre);
            ResultSet resultado = ps.executeQuery();
            if (resultado.next()) {

                mate = new Materia(resultado.getString("nombre"), resultado.getInt("año"), resultado.getBoolean("Estado"));
                mate.setIdMateria(resultado.getInt("idMateria"));

            } else {
                System.out.println("No se encontro la Materia");
            }
            ps.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar la Materia" + e.getMessage());

        }
        return mate;

    }
public Materia buscarMateriaPorId(int idMateria) {

        String sql = "SELECT * FROM materia WHERE idMateria= ?";
        Materia mate = null;
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idMateria);
            ResultSet resultado = ps.executeQuery();
            if (resultado.next()) {

                mate = new Materia(resultado.getString("nombre"), resultado.getInt("año"), resultado.getBoolean("Estado"));
                mate.setIdMateria(resultado.getInt("idMateria"));

            } else {
                System.out.println("No se encontro la Materia");
            }
            ps.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar la Materia" + e.getMessage());

        }
        return mate;

    }

    public List<Materia> listarMaterias() {

        String sql = "SELECT * FROM materia WHERE 1";
        List<Materia> materias = new ArrayList();

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet resultado = ps.executeQuery();
            while (resultado.next()) {
                Materia mate = new Materia(resultado.getString("nombre"), resultado.getInt("año"), resultado.getBoolean("Estado"));
                 mate.setIdMateria(resultado.getInt("idMateria"));
                materias.add(mate);

            }
            ps.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar las materias" + e.getMessage());
        }

        return materias;

    }

    public void actualizarMateria(Materia m) {
        String query = "UPDATE materia SET nombre = ?, año = ?, Estado = ? WHERE idMateria = ?";

        try {
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, m.getNombre());
            ps.setInt(2, m.getAnio());
            ps.setBoolean(3, m.isEstado());
            ps.setInt(4, m.getIdMateria());
            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el materia" + e.getMessage());
        }
    }

    public void borrarMateria(int id) {
        String query = "DELETE FROM materia WHERE idMateria = ?";

        try {
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            
            ps.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar materia" + e.getMessage());
        }
    }

    public void habilitarMateria(Materia m) {
        String query = "UPDATE materia SET Estado = 1 WHERE idMateria = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(query);
                ps.setInt(1, m.getIdMateria());
                ps.executeUpdate();
                
                ps.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al habilitar la materia" + e.getMessage());
        }
    }

    public void deshabilitarMateria(Materia m) {
        String query = "UPDATE materia SET Estado = 0 WHERE idMateria = ?";
        
        try {
            PreparedStatement ps = conexion.prepareStatement(query);
                ps.setInt(1, m.getIdMateria());
                ps.executeUpdate();
                
                ps.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al deshabilitar la materia" + e.getMessage());
        }
    }

}

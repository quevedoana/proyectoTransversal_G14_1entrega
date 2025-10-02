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
import java.time.LocalDate;
import java.util.ArrayList;
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

    public AlumnoData() {
        conexion = Conexion.getConexion();
    }

    public void guardarAlumno(Alumno a) { //insert
        String query = "INSERT INTO alumno( DNI, Apellido, Nombre, FechaNacimiento, Estado) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, a.getDni());
            ps.setString(2, a.getApellido());
            ps.setString(3, a.getNombre());
            ps.setDate(4, Date.valueOf(a.getFechaNacimiento()));
            ps.setBoolean(5, a.isEstado());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                a.setIdAlumno(rs.getInt(1));
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo obtener el id del alumno");
                ps.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el alumno" + e.getMessage());
        }

    }

    public Alumno buscarAlumno(int id) { //select 1 alumno
        String sql = "SELECT * FROM alumno WHERE idAlumno=?";
        Alumno alu = null;
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet resultado = ps.executeQuery();
            if (resultado.next()) {

                alu = new Alumno(resultado.getInt("DNI"), resultado.getString("Apellido"), resultado.getString("Nombre"), LocalDate.parse(resultado.getString("FechaNacimiento")), resultado.getBoolean("Estado"));
                alu.setIdAlumno(resultado.getInt("idAlumno"));

            } else {
                System.out.println("No se encontro el alumno");
            }
            ps.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el alumno" + e.getMessage());

        }
        return alu;
    }

    public List<Alumno> listarAlumnos() { //select *
        String sql = "SELECT * FROM alumno WHERE 1";
        List<Alumno> alumnos = new ArrayList();

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet resultado = ps.executeQuery();
            while (resultado.next()) {
                Alumno alu = new Alumno(resultado.getInt("DNI"), resultado.getString("Apellido"), resultado.getString("Nombre"), LocalDate.parse(resultado.getString("FechaNacimiento")), resultado.getBoolean("Estado"));
                alumnos.add(alu);

            }
            ps.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar alumno" + e.getMessage());
        }

        return alumnos;
    }
    //ACTUALIZAR ALMUNO!

    public void actualizarAlumno(Alumno a) {
        String query = "UPDATE alumno SET DNI = ?, Apellido = ?, Nombre = ?, FechaNacimiento = ?, Estado = ? WHERE id=?";

        try {
            try (PreparedStatement ps = conexion.prepareStatement(query)) {
                ps.setInt(1, a.getDni());
                ps.setString(2, a.getApellido());
                ps.setString(3, a.getNombre());
                ps.setDate(4, Date.valueOf(a.getFechaNacimiento()));
                ps.setBoolean(5, a.isEstado());
                ps.setInt(6, a.getIdAlumno());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el alumno" + e.getMessage());
        }
    }

    //BORRAR ALUMNO
    public void BorrarAlumno(Alumno a) {
        String query = "DELETE FROM alumno WHERE IdAlumno = ?";

        try {
            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, a.getIdAlumno());
            ps.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el alumno" + e.getMessage());
        }
    }
}

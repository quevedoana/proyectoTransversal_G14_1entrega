/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author maria
 */
public class Inscripcion {
    //TURCO
    private int idInscripto;
    private double nota;
    private Alumno alumno;
    
    private Materia materia;

    public Inscripcion(int idInscripto,Alumno alumno, Materia materia,double nota) {
        this.idInscripto = idInscripto;
        this.nota = nota;
        this.alumno = alumno;
        this.materia = materia;
    }
    
    

    public Inscripcion(Alumno alumno, Materia materia,int nota) {
        this.nota = nota;
        this.alumno = alumno;
        this.materia = materia;
    }
    

    

    public Inscripcion() {
    }
    

    public int getIdInscripto() {
        return idInscripto;
    }

    public void setIdInscripto(int idInscripto) {
        this.idInscripto = idInscripto;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno Alumno) {
        this.alumno = Alumno;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    @Override
    public String toString() {
        return "Inscripcion{" + "idInscripto=" + idInscripto + ", nota=" + nota + ", idAlumno=" + alumno.getIdAlumno() + ", idMateria=" + materia.getIdMateria() + '}';
    }

    

   

   

    
    
    

    
}

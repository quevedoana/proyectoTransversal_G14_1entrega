/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Alumno;
import Modelo.Conexion;
import Persistencia.AlumnoData;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Anitabonita
 */
public class TestAlumnos {
    private static Conexion miConexion;
    private static AlumnoData alumnoData;
    /**
     * @param args the command line arguments
     */
   
    public static void main(String[] args) {
       
       conectar();
       List <Alumno>listaAlumnos=new ArrayList();
       Alumno alu1=new Alumno(45563392,"Naranjo","Maria Candela",LocalDate.of(2004, Month.MAY,05),true);
       alumnoData.guardarAlumno(alu1);
       Alumno alu2=new Alumno(44075900,"Assat","Antonio Tomas",LocalDate.of(2002, Month.MARCH, 28),false);
       alumnoData.guardarAlumno(alu2);
       Alumno alu3=new Alumno(39137807,"di Fiore","Mariano Enzo",LocalDate.of(1996, Month.JANUARY, 12),true);
       alumnoData.guardarAlumno(alu3);
       Alumno alu4=new Alumno(45886496,"Barroso","Esteban Jose",LocalDate.of(2004, Month.SEPTEMBER, 16),true);
       alumnoData.guardarAlumno(alu4);
       Alumno alu5=new Alumno(43343200,"Quevedo","Ana Banana",LocalDate.of(2001, Month.FEBRUARY, 02),true);
       alumnoData.guardarAlumno(alu5);
       
       System.out.println(alumnoData.buscarAlumno(2)); // metodo buscarAlumno
       listaAlumnos=alumnoData.listarAlumnos();// metodo listarAlumno
       
        for (Alumno alumno : listaAlumnos) {
            System.out.println(alumno);
        }
        
        alu5.setNombre("Ana Paula");
        alumnoData.actualizarAlumno(alu5);
        System.out.println(alu5);
        
        //alumnoData.BorrarAlumno(9);
        //alumnoData.guardarAlumno(alu4);
        
        //alumnoData.HabilitarAlumno(alu2);
        //alumnoData.DeshabilitarAlumno(alu3);
        
    }
    
    static void conectar(){
       
       miConexion = new Conexion("jdbc:mariadb://localhost/gp14_universidadulp","root","");
       alumnoData = new AlumnoData(miConexion);
       

    }
    
}

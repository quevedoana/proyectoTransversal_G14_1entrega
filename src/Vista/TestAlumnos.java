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
       
       //List <Alumno>listaAlumno;
       Alumno alu1=new Alumno(45563392,"Naranjo","Maria Candela",LocalDate.of(2004, Month.MAY,05),true);
       conectar(alu1);
       Alumno alu2=new Alumno(44075900,"Assat","Antonio Tomas",LocalDate.of(2002, Month.MARCH, 28),true);
       conectar(alu2);
       Alumno alu3=new Alumno(39137807,"di Fiore","Mariano Enzo",LocalDate.of(1996, Month.JANUARY, 12),true);
       conectar(alu3);
       Alumno alu4=new Alumno(45886496,"Barroso","Esteban Jose",LocalDate.of(2004, Month.SEPTEMBER, 16),true);
       conectar(alu4);
       Alumno alu5=new Alumno(43343200,"Quevedo","Ana Banana",LocalDate.of(2001, Month.FEBRUARY, 02),true);
       conectar(alu5);
       
       //listaAlumno=alu.listarAlumno();
       
        /*for (Alumno alumno : listaAlumno) {
            System.out.println(alumno);
        }*/
        
    }
    
    static void conectar(Alumno a){
       miConexion = new Conexion("jdbc:mariadb://localhost/gp14_universidadulp","root","");
       alumnoData = new AlumnoData(miConexion);
       alumnoData.guardarAlumno(a);
       //Alumno alu = alumnoData.buscarAlumno(a.getIdAlumno());
       //System.out.println("Datos "+ alu);
       
       
    }
    
}

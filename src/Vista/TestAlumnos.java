/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Alumno;
import Modelo.Conexion;
import Persistencia.alumnoData;

/**
 *
 * @author Anitabonita
 */
public class TestAlumnos {
    private Conexion miConexion;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
    }
    
    void conectar(Alumno a){
       miConexion = new Conexion("jdbc:mysql://localhost/gp4_universidadulp","root","");
    }
    
}

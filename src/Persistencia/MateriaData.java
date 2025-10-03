/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Modelo.Conexion;
import Modelo.Materia;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author maria
 */
public class MateriaData {
    //ESTEBAN HACE LO QUE PUEDAS DE TODAS LAS CONSULTAS SI NO TE SALEN TODAS LAS COMPLETAMOS DESPUES
    //insertar, actualizar, buscar, baja/altaLogica, y borrar
     private Connection conexion = null;

    public MateriaData(Conexion cone) {
        this.conexion = cone.getConexion();
    }

    public MateriaData() {
    }
    
    public void guardarMateria(Materia m){
        
    }
    
    public Materia buscarMateria(String nombre){
        
         return null;
        
    }
     public List<Materia> listarMaterias(){
         
         return null;
         
     }
     
    public void actualizarMateria(Materia m){
        
    }
    
    public void borrarMateria(Materia m){
        
    }
    
    public void habilitarMateria(Materia m){
        
    }
    
    public void deshabilitarMteria(Materia m){
        
    }
    
}

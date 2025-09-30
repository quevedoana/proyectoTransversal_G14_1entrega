/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Modelo.Conexion;
import java.sql.Connection;

/**
 *
 * @author Anitabonita
 */
public class AlumnoData {
    private Connection conexion = null;
    
    public AlumnoData(Conexion conex){
        this.conexion=conex.buscarConexion();
    }
    
    
}



package Modelo;

/**
 *
 * @author maria
 */
public class Materia {
    
    private int idMateria;
    private String Nombre;
    private int Anio;
    private boolean Estado;

    public Materia(String Nombre, int Año, boolean Estado) {
        this.Nombre = Nombre;
        this.Anio = Año;
        this.Estado = Estado;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public int getAnio() {
        return Anio;
    }

    public void setAnio(int Año) {
        this.Anio = Año;
    }

    public boolean isEstado() {
        return Estado;
    }

    public void setEstado(boolean Estado) {
        this.Estado = Estado;
    }

    @Override
    public String toString() {
        return "Materia{" + "idMateria=" + idMateria + ", Nombre=" + Nombre + ", Año=" + Anio + ", Estado=" + Estado + '}';
    }
    
}

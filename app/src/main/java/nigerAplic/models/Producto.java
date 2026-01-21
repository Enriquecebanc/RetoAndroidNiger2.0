package nigerAplic.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "productos")
public class Producto {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nombre;
    private double precio;
    private String materiales;
    private String imagen; // nombre del drawable

    // Constructor vacío obligatorio para Room
    public Producto() {
    }

    // Constructor opcional (Room lo ignora)
    public Producto(int id, String nombre, double precio, String materiales, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.materiales = materiales;
        this.imagen = imagen;
    }

    // GETTERS Y SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getMateriales() {
        return materiales;
    }

    public void setMateriales(String materiales) {
        this.materiales = materiales;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}

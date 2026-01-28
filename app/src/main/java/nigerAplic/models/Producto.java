package nigerAplic.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// @Entity indica que esta clase representa una tabla en la base de datos llamada "productos"
@Entity(tableName = "productos")
public class Producto {

    // @PrimaryKey indica que este campo es el identificador único de cada producto.
    // autoGenerate = true significa que la base de datos asignará el número (1, 2,
    // 3...) automáticamente.
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Campos para guardar la información del producto
    private String nombre;
    private double precio;
    private String materiales;
    private String imagen; // Guardará el nombre del recurso o la dirección (URI) de la imagen
    private int stock;

    // Constructor vacío obligatorio para que Room (la base de datos) pueda crear
    // objetos
    public Producto() {
    }

    // Constructor opcional para crear productos manualmente desde código
    public Producto(int id, String nombre, double precio, String materiales, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.materiales = materiales;
        this.imagen = imagen;
    }

    // --- GETTERS Y SETTERS ---
    // Son métodos para leer (get) y modificar (set) los valores privados de la
    // clase de forma segura.
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Producto producto = (Producto) o;
        return id == producto.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}

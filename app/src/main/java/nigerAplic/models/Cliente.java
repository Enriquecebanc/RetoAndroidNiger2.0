package nigerAplic.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Modelo que representa a un Cliente en la base de datos.
 * Utiliza Room para la persistencia de datos.
 */
@Entity(tableName = "clientes")
public class Cliente {
    @PrimaryKey(autoGenerate = true)
    private int id; // ID autogenerado para cada cliente

    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String email;

    /**
     * Constructor para crear un nuevo cliente.
     * 
     * @param nombre    Nombre del cliente
     * @param apellido  Apellido del cliente
     * @param telefono  Teléfono de contacto
     * @param direccion Dirección física
     * @param email     Correo electrónico
     */
    public Cliente(String nombre, String apellido, String telefono, String direccion, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getEmail() {
        return email;
    }
}

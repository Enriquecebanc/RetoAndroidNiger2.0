package nigerAplic.models;

public class Cliente {
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String email;

    public Cliente(String nombre, String apellido, String telefono, String direccion, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public String getEmail() { return email; }
}

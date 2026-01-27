package nigerAplic.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pedidos")
public class Pedidin {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String clienteNombre;
    private double total;
    private String fecha;
    private String productosResumen;

    public Pedidin() {
    }

    public Pedidin(String clienteNombre, double total, String fecha, String productosResumen) {
        this.clienteNombre = clienteNombre;
        this.total = total;
        this.fecha = fecha;
        this.productosResumen = productosResumen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getProductosResumen() {
        return productosResumen;
    }

    public void setProductosResumen(String productosResumen) {
        this.productosResumen = productosResumen;
    }
}

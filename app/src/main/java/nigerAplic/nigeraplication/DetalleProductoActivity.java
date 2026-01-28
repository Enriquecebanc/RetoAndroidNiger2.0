package nigerAplic.nigeraplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nigerAplic.adapter.ProductoAdapter;
import nigerAplic.database.AppDatabase;
import nigerAplic.models.Producto;

// Esta actividad muestra detalles de productos, aunque actualmente funciona como otra lista de productos.
public class DetalleProductoActivity extends AppCompatActivity {

    private RecyclerView recyclerProductos; // La lista visual
    private ProductoAdapter adapter; // El adaptador para conectar datos con la lista
    private AppDatabase db; // Referencia a la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        // Configuración de la lista (RecyclerView)
        recyclerProductos = findViewById(R.id.recyclerProductos);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));

        // Obtener instancia de la Base de Datos
        db = AppDatabase.getInstance(this);

        // Insertar productos de prueba si la base de datos está vacía
        // Esto es útil para que el usuario vea algo la primera vez que abre la app.
        if (db.productoDao().getAll().isEmpty()) {

            Producto p1 = new Producto();
            p1.setNombre("Maceta Grande");
            p1.setPrecio(40);
            p1.setMateriales(
                    "• Led rojo: 2\n" +
                            "• Led verde: 2\n" +
                            "• Led amarillo: 2\n" +
                            "• Maceta de plástico: Grande\n" +
                            "• Sensor de humedad: 1\n" +
                            "• Sensor de luz: 1\n" +
                            "• Batería: 1");
            p1.setImagen("grande");

            Producto p2 = new Producto();
            p2.setNombre("Maceta Mediana");
            p2.setPrecio(34);
            p2.setMateriales(
                    "• Led rojo: 2\n" +
                            "• Led verde: 2\n" +
                            "• Led amarillo: 2\n" +
                            "• Maceta de plástico: Grande\n" +
                            "• Sensor de humedad: 1\n" +
                            "• Sensor de luz: 1\n" +
                            "• Batería: 1");
            p2.setImagen("mediano");

            Producto p3 = new Producto();
            p3.setNombre("Maceta Pequeña");
            p3.setPrecio(27);
            p3.setMateriales(
                    "• Led rojo: 2\n" +
                            "• Led verde: 2\n" +
                            "• Led amarillo: 2\n" +
                            "• Maceta de plástico: Grande\n" +
                            "• Sensor de humedad: 1\n" +
                            "• Sensor de luz: 1\n" +
                            "• Batería: 1");
            p3.setImagen("peque_o");

            db.productoDao().insert(p1);
            db.productoDao().insert(p2);
            db.productoDao().insert(p3);
        }

        // BOTÓN AÑADIR PRODUCTO
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DetalleProductoActivity.this,
                    AgregarProductoActivity.class);
            startActivity(intent);
        });

        //  BOTÓN IR AL CARRITO
        Button btnAd = findViewById(R.id.btnAd);
        btnAd.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DetalleProductoActivity.this,
                    CarritoActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarProductos(); // Recargar la lista al volver a esta pantalla
    }

    private void cargarProductos() {
        // --- Cargar productos ---
        // 1. Pedir la lista de productos a la base de datos
        List<Producto> productos = db.productoDao().getAll();
        // 2. Crear el adaptador con esos productos
        adapter = new ProductoAdapter(this, productos);
        // 3. Asignar el adaptador a la vista de lista
        recyclerProductos.setAdapter(adapter);
    }
}

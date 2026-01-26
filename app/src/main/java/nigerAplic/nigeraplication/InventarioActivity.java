package nigerAplic.nigeraplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import nigerAplic.adapter.ProductoAdapter;
import nigerAplic.database.AppDatabase;
import nigerAplic.models.Producto;
import nigerAplic.models.ProductoDao;

public class InventarioActivity extends AppCompatActivity {

    private RecyclerView rvProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        rvProductos = findViewById(R.id.rvProductos);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));

        // Botón flotante para agregar (Opcional)
        FloatingActionButton btnAdd = findViewById(R.id.btnAddProducto);
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(InventarioActivity.this, AgregarProductoActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarProductos();
    }

    private void cargarProductos() {
        // Cargar productos de la BD
        List<Producto> listaProductos = AppDatabase.getInstance(this).productoDao().getAll();

        // Si la lista está vacía, agregar datos de prueba (Opcional, para que el
        // usuario vea algo)
        if (listaProductos.isEmpty()) {
            agregarDatosPrueba();
            listaProductos = AppDatabase.getInstance(this).productoDao().getAll();
        }

        ProductoAdapter adapter = new ProductoAdapter(this, listaProductos);
        rvProductos.setAdapter(adapter);
    }

    private void agregarDatosPrueba() {
        ProductoDao dao = AppDatabase.getInstance(this).productoDao();

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
        p1.setStock(100);

        Producto p2 = new Producto();
        p2.setNombre("Maceta Mediana");
        p2.setPrecio(34);
        p2.setMateriales(
                "• Led rojo: 2\n" +
                        "• Led verde: 2\n" +
                        "• Led amarillo: 2\n" +
                        "• Maceta de plástico: Mediana\n" +
                        "• Sensor de humedad: 1\n" +
                        "• Sensor de luz: 1\n" +
                        "• Batería: 1");
        p2.setImagen("mediano");
        p2.setStock(100);

        Producto p3 = new Producto();
        p3.setNombre("Maceta Pequeña");
        p3.setPrecio(27);
        p3.setMateriales(
                "• Led rojo: 2\n" +
                        "• Led verde: 2\n" +
                        "• Led amarillo: 2\n" +
                        "• Maceta de plástico: Pequeña\n" +
                        "• Sensor de humedad: 1\n" +
                        "• Sensor de luz: 1\n" +
                        "• Batería: 1");
        p3.setImagen("peque_o");
        p3.setStock(100);

        dao.insert(p1);
        dao.insert(p2);
        dao.insert(p3);
    }
}
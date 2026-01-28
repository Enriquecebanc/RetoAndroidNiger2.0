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

// Esta clase representa la pantalla principal del inventario.
public class InventarioActivity extends AppCompatActivity {

    // Variable para la lista visual de productos
    private RecyclerView rvProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Carga el diseño visual desde activity_inventario.xml
        setContentView(R.layout.activity_inventario);

        // Preparamos la lista (RecyclerView) donde se verán los productos
        rvProductos = findViewById(R.id.rvProductos);
        // Decimos que los productos se muestren en una lista vertical
        rvProductos.setLayoutManager(new LinearLayoutManager(this));

        //  Configuración de botones

        // Botón flotante para ir a la pantalla de añadir producto
        FloatingActionButton btnAdd = findViewById(R.id.btnAddProducto);
        btnAdd.setOnClickListener(v -> {
            // Abrimos la actividad AgregarProductoActivity
            Intent intent = new Intent(InventarioActivity.this, AgregarProductoActivity.class);
            startActivity(intent);
        });

        // Botón flotante para ir al carrito
        FloatingActionButton btnCart = findViewById(R.id.btnGoToCart);
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(InventarioActivity.this, CarritoActivity.class);
            startActivity(intent);
        });
    }

    /*
     * Este método se llama cada vez que la pantalla vuelve a ser visible.
     * Es ideal para recargar la lista de productos por si añadimos uno nuevo.
     */
    @Override
    protected void onResume() {
        super.onResume();
        cargarProductos();
    }

    // Método para obtener los productos de la base de datos y mostrarlos
    private void cargarProductos() {
        // Pedimos a la base de datos que nos dé todos los productos guardados
        List<Producto> listaProductos = AppDatabase.getInstance(this).productoDao().getAll();

        // Si no hay productos, creamos unos de prueba para que no se vea vacío
        if (listaProductos.isEmpty()) {
            agregarDatosPrueba();
            // Volvemos a pedir la lista ahora que ya tiene datos
            listaProductos = AppDatabase.getInstance(this).productoDao().getAll();
        }

        // Creamos el adaptador (que une los datos con la vista) y se lo ponemos a la
        // lista
        ProductoAdapter adapter = new ProductoAdapter(this, listaProductos);
        rvProductos.setAdapter(adapter);
    }

    // Método auxiliar para crear datos falsos (dummy data) con fines de prueba
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
package nigerAplic.nigeraplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nigerAplic.adapter.CarritoAdapter;
import nigerAplic.models.Producto;
import nigerAplic.utils.CartManager;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView recyclerCarrito;
    private CarritoAdapter adapter;

    private android.widget.TextView tvTotalCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        recyclerCarrito = findViewById(R.id.recyclerCarrito);
        tvTotalCarrito = findViewById(R.id.tvTotalCarrito);
        recyclerCarrito.setLayoutManager(new LinearLayoutManager(this));

        android.widget.Button btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra);
        btnFinalizarCompra.setOnClickListener(v -> mostrarDialogoConfirmacion());
    }

    private void mostrarDialogoConfirmacion() {
        if (CartManager.getInstance().getAll().isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Finalizar Compra")
                .setMessage("¿Estás seguro de finalizar la compra?")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    // Actualizar stock en BD
                    List<nigerAplic.models.CartItem> items = CartManager.getInstance().getAll();
                    nigerAplic.models.ProductoDao dao = nigerAplic.database.AppDatabase.getInstance(this).productoDao();

                    for (nigerAplic.models.CartItem item : items) {
                        nigerAplic.models.Producto p = item.getProducto();
                        int cantidadComprada = item.getQuantity();
                        int nuevoStock = p.getStock() - cantidadComprada;
                        if (nuevoStock < 0)
                            nuevoStock = 0;

                        p.setStock(nuevoStock);
                        dao.update(p);
                    }

                    CartManager.getInstance().clear();
                    actualizarTotal();
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                        cargarProductos(); // Recargar lista vacía
                    }
                    Toast.makeText(this, "Compra realizada con éxito", Toast.LENGTH_LONG).show();

                    // Navegar al menú principal
                    android.content.Intent intent = new android.content.Intent(CarritoActivity.this,
                            MainActivity.class);
                    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // Cerrar la actividad actual
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarProductos();
    }

    private void cargarProductos() {
        List<nigerAplic.models.CartItem> listaCarrito = CartManager.getInstance().getAll();

        if (listaCarrito.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
        }

        actualizarTotal();

        adapter = new CarritoAdapter(this, listaCarrito, () -> {
            actualizarTotal();
        });
        recyclerCarrito.setAdapter(adapter);
    }

    private void actualizarTotal() {
        double total = CartManager.getInstance().calculateTotal();
        tvTotalCarrito.setText(String.format("Total: %.2f €", total));
    }
}

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarProductos();
    }

    private void cargarProductos() {
        List<Producto> listaCarrito = CartManager.getInstance().getAll();

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

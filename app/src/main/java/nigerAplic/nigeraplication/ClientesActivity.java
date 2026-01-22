package nigerAplic.nigeraplication;

import nigerAplic.adapter.ClienteAdapter;
import nigerAplic.models.Cliente;
import nigerAplic.database.AppDatabase;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ClientesActivity extends AppCompatActivity {

    RecyclerView rvClientes;
    ClienteAdapter adapter;
    List<Cliente> listaClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        rvClientes = findViewById(R.id.rvClientes);
        rvClientes.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar lista vacía, se cargará en onResume
        listaClientes = new ArrayList<>();
        adapter = new ClienteAdapter(listaClientes, this::eliminarCliente, this::mostrarOpciones);
        rvClientes.setAdapter(adapter);

        FloatingActionButton btnAdd = findViewById(R.id.btnAddCliente);
        btnAdd.setOnClickListener(v -> {
            // Aquí iría el Intent para abrir el formulario
            Intent intent = new Intent(ClientesActivity.this, FormClienteActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarClientes();
    }

    private void cargarClientes() {
        listaClientes = AppDatabase.getInstance(this).clienteDao().getAll();
        // Actualizar el adaptador con la nueva lista
        adapter = new ClienteAdapter(listaClientes, this::eliminarCliente, this::mostrarOpciones);
        rvClientes.setAdapter(adapter);
    }

    private void eliminarCliente(Cliente cliente) {
        AppDatabase.getInstance(this).clienteDao().delete(cliente);
        Toast.makeText(this, "Cliente eliminado", Toast.LENGTH_SHORT).show();
        cargarClientes();
    }

    private void mostrarOpciones(Cliente cliente) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Opciones para " + cliente.getNombre());
        builder.setItems(new CharSequence[] { "Ver ubicación", "Llamar" }, (dialog, which) -> {
            switch (which) {
                case 0:
                    verEnMapa(cliente);
                    break;
                case 1:
                    llamarCliente(cliente);
                    break;
            }
        });
        builder.show();
    }

    private void verEnMapa(Cliente cliente) {
        String direccion = cliente.getDireccion();
        if (direccion != null && !direccion.isEmpty()) {
            android.net.Uri gmmIntentUri = android.net.Uri.parse("geo:0,0?q=" + android.net.Uri.encode(direccion));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Try generic map intent if Google Maps not installed
                Intent genericMapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(genericMapIntent);
            }
        } else {
            Toast.makeText(this, "El cliente no tiene dirección", Toast.LENGTH_SHORT).show();
        }
    }

    private void llamarCliente(Cliente cliente) {
        String telefono = cliente.getTelefono();
        if (telefono != null && !telefono.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(android.net.Uri.parse("tel:" + telefono));
            startActivity(intent);
        } else {
            Toast.makeText(this, "El cliente no tiene teléfono", Toast.LENGTH_SHORT).show();
        }
    }
}

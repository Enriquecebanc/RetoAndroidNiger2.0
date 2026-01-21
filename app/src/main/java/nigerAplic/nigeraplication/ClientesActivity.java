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
        adapter = new ClienteAdapter(listaClientes, this::eliminarCliente);
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
        adapter = new ClienteAdapter(listaClientes, this::eliminarCliente);
        rvClientes.setAdapter(adapter);
    }

    private void eliminarCliente(Cliente cliente) {
        AppDatabase.getInstance(this).clienteDao().delete(cliente);
        Toast.makeText(this, "Cliente eliminado", Toast.LENGTH_SHORT).show();
        cargarClientes();
    }
}

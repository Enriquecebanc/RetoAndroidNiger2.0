public class ClientesActivity extends AppCompatActivity {

    RecyclerView rvClientes;
    ClienteAdapter adapter;
    List<Cliente> listaClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        rvClientes = findViewById(R.id.rvClientes);
        rvClientes.setLayoutManager(new LinearLayoutManager(this));

        // Lista de prueba
        listaClientes = new ArrayList<>();
        listaClientes.add(new Cliente("Juan", "Pérez", "123456789", "Calle Falsa 123", "juan@mail.com"));
        listaClientes.add(new Cliente("Ana", "Gómez", "987654321", "Av. Siempre Viva 456", "ana@mail.com"));

        adapter = new ClienteAdapter(listaClientes);
        rvClientes.setAdapter(adapter);

        FloatingActionButton btnAdd = findViewById(R.id.btnAddCliente);
        btnAdd.setOnClickListener(v -> {
            // Aquí iría el Intent para abrir el formulario
            Intent intent = new Intent(ClientesActivity.this, FormClienteActivity.class);
            startActivity(intent);
        });
    }
}

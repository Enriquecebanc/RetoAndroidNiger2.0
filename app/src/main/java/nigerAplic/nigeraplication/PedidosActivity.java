package nigerAplic.nigeraplication;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import nigerAplic.adapter.PedidosAdapter;
import nigerAplic.database.AppDatabase;
import nigerAplic.models.Pedidin;
import nigerAplic.models.PedidinDao;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;
import nigerAplic.models.Cliente;
import nigerAplic.models.ClienteDao;

public class PedidosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        RecyclerView recyclerPedidos = findViewById(R.id.recyclerPedidos);
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getInstance(this);
        PedidinDao dao = db.pedidinDao();
        ClienteDao clienteDao = db.clienteDao();
        List<Pedidin> pedidos = dao.getAll();

        if (pedidos.isEmpty()) {
            Toast.makeText(this, "No hay pedidos registrados", Toast.LENGTH_SHORT).show();
        }

        PedidosAdapter adapter = new PedidosAdapter(pedidos, pedido -> {
            mostrarOpcionesPedido(pedido, clienteDao);
        });
        recyclerPedidos.setAdapter(adapter);
    }

    private void mostrarOpcionesPedido(Pedidin pedido, ClienteDao clienteDao) {
        Cliente cliente = clienteDao.getClienteByFullName(pedido.getClienteNombre());

        if (cliente == null || cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            Toast.makeText(this, "No se encontró el email del cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Opciones de Pedido")
                .setMessage("¿Deseas enviar el resumen del pedido por correo a " + cliente.getEmail() + "?")
                .setPositiveButton("Enviar Correo", (dialog, which) -> {
                    enviarCorreo(pedido, cliente.getEmail());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void enviarCorreo(Pedidin pedido, String email) {
        String asunto = "Resumen de su pedido - " + pedido.getFecha();
        String cuerpo = "Hola " + pedido.getClienteNombre() + ",\n\n" +
                "Este es el resumen de su pedido realizado el " + pedido.getFecha() + ":\n\n" +
                "Productos: " + pedido.getProductosResumen() + "\n" +
                "Total: " + String.format("%.2f €", pedido.getTotal()) + "\n\n" +
                "Gracias por su compra.";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
        intent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        intent.putExtra(Intent.EXTRA_TEXT, cuerpo);

        try {
            startActivity(Intent.createChooser(intent, "Selecciona una aplicación de correo"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No hay aplicaciones de correo instaladas", Toast.LENGTH_SHORT).show();
        }
    }
}

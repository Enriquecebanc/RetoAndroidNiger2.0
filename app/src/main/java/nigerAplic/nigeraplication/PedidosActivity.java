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

// Pantalla que muestra el historial de pedidos realizados
// Permite enviar el resumen de un pedido por correo al cliente
public class PedidosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        // Configurar el RecyclerView para mostrar la lista de pedidos
        RecyclerView recyclerPedidos = findViewById(R.id.recyclerPedidos);
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));

        // Obtener los pedidos de la base de datos
        AppDatabase db = AppDatabase.getInstance(this);
        PedidinDao dao = db.pedidinDao();
        ClienteDao clienteDao = db.clienteDao();
        List<Pedidin> pedidos = dao.getAll();

        if (pedidos.isEmpty()) {
            Toast.makeText(this, "No hay pedidos registrados", Toast.LENGTH_SHORT).show();
        }

        // Configurar el adaptador con un listener para manejar clics en pedidos
        PedidosAdapter adapter = new PedidosAdapter(pedidos, pedido -> {
            mostrarOpcionesPedido(pedido, clienteDao);
        });
        recyclerPedidos.setAdapter(adapter);
    }

    // Muestra un diálogo con opciones para el pedido seleccionado
    private void mostrarOpcionesPedido(Pedidin pedido, ClienteDao clienteDao) {
        // Buscar el cliente asociado al pedido
        Cliente cliente = clienteDao.getClienteByFullName(pedido.getClienteNombre());

        if (cliente == null || cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            Toast.makeText(this, "No se encontró el email del cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar diálogo para confirmar el envío del correo
        new AlertDialog.Builder(this)
                .setTitle("Opciones de Pedido")
                .setMessage("¿Deseas enviar el resumen del pedido por correo a " + cliente.getEmail() + "?")
                .setPositiveButton("Enviar Correo", (dialog, which) -> {
                    enviarCorreo(pedido, cliente.getEmail());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Abre la aplicación de correo para enviar el resumen del pedido
    private void enviarCorreo(Pedidin pedido, String email) {
        // Preparar el asunto y el cuerpo del correo
        String asunto = "Resumen de su pedido - " + pedido.getFecha();
        String cuerpo = "Hola " + pedido.getClienteNombre() + ",\n\n" +
                "Este es el resumen de su pedido realizado el " + pedido.getFecha() + ":\n\n" +
                "Productos: " + pedido.getProductosResumen() + "\n" +
                "Total: " + String.format("%.2f €", pedido.getTotal()) + "\n\n" +
                "Gracias por su compra.";

        // Crear intent para enviar correo
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

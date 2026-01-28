package nigerAplic.nigeraplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

// Pantalla principal de la aplicación (menú principal)
// Muestra botones para navegar a las diferentes secciones de la app
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vincular los botones del layout con las variables
        android.view.View btnClientes = findViewById(R.id.btnClientes);
        android.view.View btnCarrito = findViewById(R.id.btnCarrito);
        android.view.View btnDescargas = findViewById(R.id.btnDescargas);
        android.view.View btnProductos = findViewById(R.id.btnProductos);
        android.view.View btnPedidos = findViewById(R.id.btnPedidos);
        ImageView imgMapa = findViewById(R.id.imgMapa);

        // Botón para ir a la pantalla de gestión de clientes
        btnClientes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClientesActivity.class);
            startActivity(intent);
        });

        // Botón para ir a la pantalla del carrito de compras
        btnCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CarritoActivity.class);
            startActivity(intent);
        });

        // Botón para ir a la pantalla de descargas (exportar datos a CSV)
        btnDescargas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DescargasActivity.class);
            startActivity(intent);
        });

        // Botón para ir a la pantalla del inventario de productos
        btnProductos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InventarioActivity.class);
            startActivity(intent);
        });

        // Botón para ir a la pantalla de historial de pedidos
        btnPedidos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PedidosActivity.class);
            startActivity(intent);
        });

        // Configurar la imagen del mapa como botón interactivo
        // Al hacer clic, abre Google Maps con la ubicación de la empresa
        imgMapa.setOnClickListener(v -> {
            // Coordenadas de Mancomunidad del Alto Deba
            Uri gmmIntentUri = Uri.parse("geo:43.0639,-2.4848?q=Mancomunidad+del+Alto+Deba");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

            // Intentar abrir Google Maps
            mapIntent.setPackage("com.google.android.apps.maps");

            // Si Google Maps está instalado, lo abre
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Si no está instalado, abre en el navegador web
                Intent webIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(webIntent);
            }
        });
    }
}
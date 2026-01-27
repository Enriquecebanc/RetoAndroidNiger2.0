package nigerAplic.nigeraplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enlace de componentes con el XML
        // Enlace de componentes con el XML (usamos View para permitir cambiar el tipo
        // de botón en XML)
        android.view.View btnClientes = findViewById(R.id.btnClientes);
        android.view.View btnCarrito = findViewById(R.id.btnCarrito);
        android.view.View btnDescargas = findViewById(R.id.btnDescargas);
        android.view.View btnProductos = findViewById(R.id.btnProductos);
        ImageView imgMapa = findViewById(R.id.imgMapa);

        // Navegación de botones
        btnClientes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClientesActivity.class);
            startActivity(intent);
        });

        btnCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CarritoActivity.class);
            startActivity(intent);
        });

        btnDescargas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DescargasActivity.class);
            startActivity(intent);
        });

        btnProductos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InventarioActivity.class);
            startActivity(intent);
        });

        android.view.View btnPedidos = findViewById(R.id.btnPedidos);
        btnPedidos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PedidosActivity.class);
            startActivity(intent);
        });

        // Lógica para el mapa (imagen interactiva)
        imgMapa.setOnClickListener(v -> {
            // Coordenadas de Mancomunidad del Alto Deba
            Uri gmmIntentUri = Uri.parse("geo:43.0639,-2.4848?q=Mancomunidad+del+Alto+Deba");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

            // Intentar abrir la aplicación de Google Maps
            mapIntent.setPackage("com.google.android.apps.maps");

            // Si el dispositivo tiene Google Maps instalado, lo abre
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Si no, abre el navegador web normal
                Intent webIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(webIntent);
            }
        });
    }
}
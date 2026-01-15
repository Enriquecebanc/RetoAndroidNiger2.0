package nigerAplic.nigeraplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnClientes = findViewById(R.id.btnClientes);
        Button btnCarrito = findViewById(R.id.btnCarrito);
        Button btnDescargas = findViewById(R.id.btnDescargas);
        Button btnDetalle = findViewById(R.id.btnDetalle);

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

        btnDetalle.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DetalleProductoActivity.class);
            startActivity(intent);
        });
    }
}
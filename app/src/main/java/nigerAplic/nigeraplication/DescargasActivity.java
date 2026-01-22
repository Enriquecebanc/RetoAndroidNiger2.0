package nigerAplic.nigeraplication; // REVISA que este sea el nombre de tu paquete

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DescargasActivity extends AppCompatActivity {

    private Button btnDescargaMasiva;
    private ProgressBar pbDescarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Esto es lo que conecta el código con tu XML actual
        setContentView(R.layout.activity_descargas);

        // Inicializamos los componentes del XML
        //btnDescargaMasiva = findViewById(R.id.btnDescargaMasiva);
        pbDescarga = findViewById(R.id.pbDescarga);

        // Acción al pulsar el botón
        btnDescargaMasiva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarDescargas();
            }
        });
    }

    private void realizarDescargas() {
        // Mostramos la barra de carga
        pbDescarga.setVisibility(View.VISIBLE);
        btnDescargaMasiva.setEnabled(false);
        btnDescargaMasiva.setText("Descargando...");

        // Aquí simulamos la lógica de descarga
        // En el futuro aquí irán tus llamadas a la base de datos de Clientes y Pedidos
        Toast.makeText(this, "Descargando clientes y pedidos...", Toast.LENGTH_LONG).show();

        // Simulación: detener la carga a los 3 segundos
        btnDescargaMasiva.postDelayed(() -> {
            pbDescarga.setVisibility(View.GONE);
            btnDescargaMasiva.setEnabled(true);
            btnDescargaMasiva.setText("DESCARGAR TODO\n(Clientes, Pedidos, etc.)");
            Toast.makeText(DescargasActivity.this, "Descarga completada", Toast.LENGTH_SHORT).show();
        }, 3000);
    }
}
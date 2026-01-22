package nigerAplic.nigeraplication; // REVISA que este sea el nombre de tu paquete

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DescargasActivity extends AppCompatActivity {

    private Button btnDescargarClientes, btnDescargarPedidos, btnDescargarProductos;
    private ProgressBar pbDescarga;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descargas);

        // Inicializar componentes
        btnDescargarClientes = findViewById(R.id.btnDescargarClientes);
        btnDescargarPedidos = findViewById(R.id.btnDescargarPedidos);
        btnDescargarProductos = findViewById(R.id.btnDescargarProductos);
        pbDescarga = findViewById(R.id.pbDescarga);

        // Listeners
        btnDescargarClientes.setOnClickListener(v -> descargarClientes());
        btnDescargarPedidos.setOnClickListener(v -> descargarPedidos());
        btnDescargarProductos.setOnClickListener(v -> descargarProductos());
    }

    private void descargarClientes() {
        iniciarDescarga(btnDescargarClientes, "Descargando clientes...", "Clientes descargados correctamente");
    }

    private void descargarPedidos() {
        iniciarDescarga(btnDescargarPedidos, "Descargando pedidos...", "Pedidos descargados correctamente");
    }

    private void descargarProductos() {
        iniciarDescarga(btnDescargarProductos, "Descargando productos...", "Productos descargados correctamente");
    }

    private void iniciarDescarga(Button btn, String mensajeInicio, String mensajeFin) {
        pbDescarga.setVisibility(View.VISIBLE);
        btn.setEnabled(false);
        Toast.makeText(this, mensajeInicio, Toast.LENGTH_SHORT).show();

        // Simulación de descarga
        new android.os.Handler().postDelayed(() -> {
            pbDescarga.setVisibility(View.GONE);
            btn.setEnabled(true);
            Toast.makeText(DescargasActivity.this, mensajeFin, Toast.LENGTH_SHORT).show();
        }, 2000);
    }
}
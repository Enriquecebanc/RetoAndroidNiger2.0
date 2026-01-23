package nigerAplic.nigeraplication; // REVISA que este sea el nombre de tu paquete

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import nigerAplic.nigeraplication.R;

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
        new Thread(() -> {
            runOnUiThread(() -> {
                pbDescarga.setVisibility(View.VISIBLE);
                btnDescargarClientes.setEnabled(false);
            });

            try {
                List<nigerAplic.models.Cliente> clientes = nigerAplic.database.AppDatabase.getInstance(this)
                        .clienteDao().getAll();
                StringBuilder csvData = new StringBuilder();
                csvData.append("ID,Nombre,Apellido,Telefono,Direccion,Email\n");

                for (nigerAplic.models.Cliente c : clientes) {
                    csvData.append(c.getId()).append(",")
                            .append(escapeCsv(c.getNombre())).append(",")
                            .append(escapeCsv(c.getApellido())).append(",")
                            .append(escapeCsv(c.getTelefono())).append(",")
                            .append(escapeCsv(c.getDireccion())).append(",")
                            .append(escapeCsv(c.getEmail())).append("\n");
                }

                saveToCsv("clientes.csv", csvData.toString());

                runOnUiThread(() -> {
                    Toast.makeText(this, "Guardado en Descargas: clientes.csv", Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error al descargar clientes", Toast.LENGTH_SHORT).show());
            } finally {
                runOnUiThread(() -> {
                    pbDescarga.setVisibility(View.GONE);
                    btnDescargarClientes.setEnabled(true);
                });
            }
        }).start();
    }

    private void descargarPedidos() {
        new Thread(() -> {
            runOnUiThread(() -> {
                pbDescarga.setVisibility(View.VISIBLE);
                btnDescargarPedidos.setEnabled(false);
            });

            try {
                // Usamos CartManager como fuente de "pedidos" actual
                List<nigerAplic.models.Producto> pedidos = nigerAplic.utils.CartManager.getInstance().getAll();
                StringBuilder csvData = new StringBuilder();
                csvData.append("ID,Producto,Precio,Materiales\n");

                for (nigerAplic.models.Producto p : pedidos) {
                    csvData.append(p.getId()).append(",")
                            .append(escapeCsv(p.getNombre())).append(",")
                            .append(p.getPrecio()).append(",")
                            .append(escapeCsv(p.getMateriales())).append("\n");
                }

                saveToCsv("pedidos.csv", csvData.toString());

                runOnUiThread(() -> {
                    Toast.makeText(this, "Guardado en Descargas: pedidos.csv", Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error al descargar pedidos", Toast.LENGTH_SHORT).show());
            } finally {
                runOnUiThread(() -> {
                    pbDescarga.setVisibility(View.GONE);
                    btnDescargarPedidos.setEnabled(true);
                });
            }
        }).start();
    }

    private void descargarProductos() {
        new Thread(() -> {
            runOnUiThread(() -> {
                pbDescarga.setVisibility(View.VISIBLE);
                btnDescargarProductos.setEnabled(false);
            });

            try {
                List<nigerAplic.models.Producto> productos = nigerAplic.database.AppDatabase.getInstance(this)
                        .productoDao().getAll();
                StringBuilder csvData = new StringBuilder();
                csvData.append("ID,Nombre,Precio,Materiales,Imagen\n");

                for (nigerAplic.models.Producto p : productos) {
                    csvData.append(p.getId()).append(",")
                            .append(escapeCsv(p.getNombre())).append(",")
                            .append(p.getPrecio()).append(",")
                            .append(escapeCsv(p.getMateriales())).append(",")
                            .append(escapeCsv(p.getImagen())).append("\n");
                }

                saveToCsv("productos.csv", csvData.toString());

                runOnUiThread(() -> {
                    Toast.makeText(this, "Guardado en Descargas: productos.csv", Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error al descargar productos", Toast.LENGTH_SHORT).show());
            } finally {
                runOnUiThread(() -> {
                    pbDescarga.setVisibility(View.GONE);
                    btnDescargarProductos.setEnabled(true);
                });
            }
        }).start();
    }

    private void saveToCsv(String fileName, String data) throws java.io.IOException {
        android.content.ContentValues values = new android.content.ContentValues();
        values.put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        values.put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "text/csv");
        values.put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_DOWNLOADS);

        android.net.Uri uri = getContentResolver().insert(android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                values);
        if (uri != null) {
            try (java.io.OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                if (outputStream != null) {
                    outputStream.write(data.getBytes());
                }
            }
        } else {
            throw new java.io.IOException("No se pudo crear el archivo en Descargas");
        }
    }

    private String escapeCsv(String data) {
        if (data == null)
            return "";
        String escaped = data.replaceAll("\"", "\"\"");
        if (data.contains(",") || data.contains("\n") || data.contains("\"")) {
            return "\"" + escaped + "\"";
        }
        return data;
    }
}
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
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import android.util.Log;

import nigerAplic.nigeraplication.R;

public class DescargasActivity extends AppCompatActivity {

    private Button btnDescargarClientes, btnDescargarPedidos, btnDescargarProductos;
    private ProgressBar pbDescarga;
    private static final int REQUEST_WRITE_PERMISSION = 100;
    private Runnable pendingAction;

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
        btnDescargarClientes.setOnClickListener(v -> checkPermissionAndDownload(this::descargarClientes));
        btnDescargarPedidos.setOnClickListener(v -> checkPermissionAndDownload(this::descargarPedidos));
        btnDescargarProductos.setOnClickListener(v -> checkPermissionAndDownload(this::descargarProductos));
    }

    private void checkPermissionAndDownload(Runnable action) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                pendingAction = action;
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        REQUEST_WRITE_PERMISSION);
            } else {
                action.run();
            }
        } else {
            action.run();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (pendingAction != null) {
                    pendingAction.run();
                    pendingAction = null;
                }
            } else {
                Toast.makeText(this, "Permiso necesario para descargar archivos en versiones anteriores de Android",
                        Toast.LENGTH_LONG).show();
            }
        }
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
                List<nigerAplic.models.CartItem> cartItems = nigerAplic.utils.CartManager.getInstance().getAll();
                StringBuilder csvData = new StringBuilder();
                csvData.append("ID,Producto,Precio,Materiales,Cantidad\n");

                for (nigerAplic.models.CartItem item : cartItems) {
                    nigerAplic.models.Producto p = item.getProducto();
                    csvData.append(p.getId()).append(",")
                            .append(escapeCsv(p.getNombre())).append(",")
                            .append(p.getPrecio()).append(",")
                            .append(escapeCsv(p.getMateriales())).append(",")
                            .append(item.getQuantity()).append("\n");
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            android.content.ContentValues values = new android.content.ContentValues();
            values.put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            values.put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "text/csv");
            values.put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH,
                    android.os.Environment.DIRECTORY_DOWNLOADS);

            android.net.Uri uri = getContentResolver().insert(
                    android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI,
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
        } else {
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs();
            }
            File file = new File(downloadsDir, fileName);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(data);
            }
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
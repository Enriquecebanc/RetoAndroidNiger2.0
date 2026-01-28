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

        // Inicializar componentes de la UI
        btnDescargarClientes = findViewById(R.id.btnDescargarClientes);
        btnDescargarPedidos = findViewById(R.id.btnDescargarPedidos);
        btnDescargarProductos = findViewById(R.id.btnDescargarProductos);
        pbDescarga = findViewById(R.id.pbDescarga);

        // Configurar Listeners a los botones
        // Se utiliza 'checkPermissionAndDownload' para verificar permisos antes de
        // iniciar la descarga
        btnDescargarClientes.setOnClickListener(v -> checkPermissionAndDownload(this::descargarClientes));
        btnDescargarPedidos.setOnClickListener(v -> checkPermissionAndDownload(this::descargarPedidos));
        btnDescargarProductos.setOnClickListener(v -> checkPermissionAndDownload(this::descargarProductos));
    }

    /**
     * Verifica si se tienen permisos de escritura en almacenamiento externo.
     * Si la versión de Android es menor a Q (Android 10), se requiere pedir
     * permisos explícitamente.
     * En Android 10+ (Scoped Storage), no es necesario permiso para escribir en la
     * carpeta de Descargas pública.
     *
     * @param action La acción (descarga) a ejecutar si se tienen permisos.
     */
    private void checkPermissionAndDownload(Runnable action) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            // Para Android 9 y versiones inferiores
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Si no hay permiso, guardamos la acción pendiente y solicitamos el permiso
                pendingAction = action;
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        REQUEST_WRITE_PERMISSION);
            } else {
                // Si ya hay permiso, ejecutamos la acción
                action.run();
            }
        } else {
            // Para Android 10+, no necesitamos WRITE_EXTERNAL_STORAGE para MediaStore
            action.run();
        }
    }

    /**
     * Maneja la respuesta del usuario a la solicitud de permisos.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, ejecutar la acción pendiente
                if (pendingAction != null) {
                    pendingAction.run();
                    pendingAction = null;
                }
            } else {
                // Permiso denegado
                Toast.makeText(this, "Permiso necesario para descargar archivos en versiones anteriores de Android",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Descarga la lista de clientes en formato CSV.
     * Se ejecuta en un hilo secundario para no bloquear la UI.
     */
    private void descargarClientes() {
        new Thread(() -> {
            // Actualizar UI: mostrar ProgressBar y deshabilitar botón
            runOnUiThread(() -> {
                pbDescarga.setVisibility(View.VISIBLE);
                btnDescargarClientes.setEnabled(false);
            });

            try {
                // Obtener datos de clientes de la base de datos (Room)
                List<nigerAplic.models.Cliente> clientes = nigerAplic.database.AppDatabase.getInstance(this)
                        .clienteDao().getAll();

                // Construir el contenido CSV
                StringBuilder csvData = new StringBuilder();
                csvData.append("ID,Nombre,Apellido,Telefono,Direccion,Email\n"); // Cabecera

                for (nigerAplic.models.Cliente c : clientes) {
                    csvData.append(c.getId()).append(",")
                            .append(escapeCsv(c.getNombre())).append(",")
                            .append(escapeCsv(c.getApellido())).append(",")
                            .append(escapeCsv(c.getTelefono())).append(",")
                            .append(escapeCsv(c.getDireccion())).append(",")
                            .append(escapeCsv(c.getEmail())).append("\n");
                }

                // Guardar el archivo
                saveToCsv("clientes.csv", csvData.toString());

                // Notificar éxito en el hilo principal
                runOnUiThread(() -> {
                    Toast.makeText(this, "Guardado en Descargas: clientes.csv", Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error al descargar clientes", Toast.LENGTH_SHORT).show());
            } finally {
                // Restaurar estado de la UI
                runOnUiThread(() -> {
                    pbDescarga.setVisibility(View.GONE);
                    btnDescargarClientes.setEnabled(true);
                });
            }
        }).start();
    }

    /**
     * Descarga la lista de pedidos en formato CSV.
     */
    private void descargarPedidos() {
        new Thread(() -> {
            runOnUiThread(() -> {
                pbDescarga.setVisibility(View.VISIBLE);
                btnDescargarPedidos.setEnabled(false);
            });

            try {
                // Obtener todos los pedidos de la base de datos
                List<nigerAplic.models.Pedidin> pedidos = nigerAplic.database.AppDatabase.getInstance(this)
                        .pedidinDao().getAll();

                // Construir CSV
                StringBuilder csvData = new StringBuilder();
                csvData.append("ID,Cliente,Total,Fecha,Productos\n");

                for (nigerAplic.models.Pedidin p : pedidos) {
                    csvData.append(p.getId()).append(",")
                            .append(escapeCsv(p.getClienteNombre())).append(",")
                            .append(p.getTotal()).append(",")
                            .append(escapeCsv(p.getFecha())).append(",")
                            .append(escapeCsv(p.getProductosResumen())).append("\n");
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

    /**
     * Descarga la lista de productos (inventario) en formato CSV.
     */
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

    /**
     * Guarda los datos en un archivo CSV en la carpeta pública de Descargas.
     * Utiliza MediaStore para Android 10+ (Q) y File API tradicional para versiones
     * anteriores.
     *
     * @param fileName Nombre del archivo.
     * @param data     Contenido del archivo.
     * @throws java.io.IOException Si ocurre un error de escritura.
     */
    private void saveToCsv(String fileName, String data) throws java.io.IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Uso de MediaStore para Android 10 y superior (Scoped Storage)
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
            // Uso de File API tradicional para versiones anteriores a Android 10
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

    /**
     * Escapa caracteres especiales para el formato CSV (comillas dobles, comas,
     * saltos de línea).
     * 
     * @param data Cadena original.
     * @return Cadena escapada.
     */
    private String escapeCsv(String data) {
        if (data == null)
            return "";
        // Reemplazar comillas dobles por dobles comillas dobles
        String escaped = data.replaceAll("\"", "\"\"");
        // Si contiene comas, saltos de línea o comillas, envolver en comillas
        if (data.contains(",") || data.contains("\n") || data.contains("\"")) {
            return "\"" + escaped + "\"";
        }
        return data;
    }
}
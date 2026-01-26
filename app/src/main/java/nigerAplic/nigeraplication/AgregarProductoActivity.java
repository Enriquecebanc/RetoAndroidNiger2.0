package nigerAplic.nigeraplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import nigerAplic.database.AppDatabase;
import nigerAplic.models.Producto;
import nigerAplic.models.ProductoDao;

public class AgregarProductoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etNombre, etPrecio, etMateriales, etStock;
    private TextView tvImagenSeleccionada;
    private Button btnSeleccionarImagen, btnGuardar;

    private Uri imagenUri; // URI de la imagen seleccionada
    private String nombreImagen; // Nombre de la imagen seleccionada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        // Vincular vistas
        etNombre = findViewById(R.id.etNombre);
        etPrecio = findViewById(R.id.etPrecio);
        etStock = findViewById(R.id.etStock);
        etMateriales = findViewById(R.id.etMateriales);
        tvImagenSeleccionada = findViewById(R.id.tvImagenSeleccionada);
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);
        btnGuardar = findViewById(R.id.btnGuardar);

        btnSeleccionarImagen.setOnClickListener(v -> abrirGaleria());
        btnGuardar.setOnClickListener(v -> guardarProducto());
    }

    private void abrirGaleria() {
        // Usar ACTION_OPEN_DOCUMENT en lugar de GET_CONTENT para obtener permisos
        // persistentes
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagenUri = data.getData();

            // IMPORTANTE: Persistir el permiso de lectura para acceder a la URI en el
            // futuro
            try {
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                getContentResolver().takePersistableUriPermission(imagenUri, takeFlags);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Obtener nombre de archivo de la URI
            nombreImagen = "Desconocida";
            Cursor cursor = getContentResolver().query(imagenUri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex >= 0) {
                    nombreImagen = cursor.getString(nameIndex);
                }
                cursor.close();
            }

            tvImagenSeleccionada.setText(nombreImagen);
        }
    }

    private void guardarProducto() {
        String nombre = etNombre.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String materiales = etMateriales.getText().toString().trim();

        if (nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty() || materiales.isEmpty()
                || nombreImagen == null) {
            Toast.makeText(this, "Completa todos los campos y selecciona una imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        int stock;
        try {
            precio = Double.parseDouble(precioStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Precio o stock inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Usar Room para guardar
        try {
            Producto nuevoProducto = new Producto();
            nuevoProducto.setNombre(nombre);
            nuevoProducto.setPrecio(precio);
            nuevoProducto.setStock(stock);
            nuevoProducto.setMateriales(materiales);
            // GUARDAR LA URI COMPLETA (como String), no solo el nombre del archivo
            nuevoProducto.setImagen(imagenUri.toString());

            ProductoDao dao = AppDatabase.getInstance(this).productoDao();
            dao.insert(nuevoProducto);

            Toast.makeText(this, "Producto guardado correctamente", Toast.LENGTH_LONG).show();

            // Limpiar campos
            etNombre.setText("");
            etPrecio.setText("");
            etMateriales.setText("");
            tvImagenSeleccionada.setText("No se ha seleccionado imagen");
            imagenUri = null;
            nombreImagen = null;

            // Opcional: Cerrar actividad para volver a la lista
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

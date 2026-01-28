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

// Esta clase representa la pantalla donde el usuario puede añadir un nuevo producto.
// Extiende AppCompatActivity para tener las funciones básicas de una pantalla de Android.
public class AgregarProductoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    // Variables para guardar las referencias a los elementos de la pantalla (cajas
    // de texto, botones...)
    private EditText etNombre, etPrecio, etMateriales, etStock;
    private TextView tvImagenSeleccionada;
    private Button btnSeleccionarImagen, btnGuardar;

    // Variables para manejar la imagen seleccionada
    private Uri imagenUri; // Aquí guardaremos la dirección de la imagen en el teléfono
    private String nombreImagen; // Aquí guardaremos el nombre del archivo de imagen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Le dice a la actividad que use el diseño visual definido en
        // 'activity_agregar_producto.xml'
        setContentView(R.layout.activity_agregar_producto);

        // --- Vincular vistas ---
        // Buscamos cada elemento en el diseño por su ID y lo guardamos en las variables
        etNombre = findViewById(R.id.etNombre);
        etPrecio = findViewById(R.id.etPrecio);
        etStock = findViewById(R.id.etStock);
        etMateriales = findViewById(R.id.etMateriales);
        tvImagenSeleccionada = findViewById(R.id.tvImagenSeleccionada);
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);
        btnGuardar = findViewById(R.id.btnGuardar);

        // --- Configurar botones ---
        // Le decimos qué hacer cuando se pulsa el botón "Seleccionar imagen"
        btnSeleccionarImagen.setOnClickListener(v -> abrirGaleria());
        // Le decimos qué hacer cuando se pulsa el botón "Guardar"
        btnGuardar.setOnClickListener(v -> guardarProducto());
    }

    // Este método abre la galería del teléfono para elegir una foto
    private void abrirGaleria() {
        // Creamos una "intención" (Intent) para abrir documentos
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*"); // Especificamos que solo queremos ver imágenes
        // Iniciamos la actividad de galería esperando un resultado (la imagen elegida)
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Este método se ejecuta automáticamente cuando volvemos de la galería
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verificamos que todo salió bien y que tenemos datos
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagenUri = data.getData(); // Guardamos la dirección de la imagen elegida

            // IMPORTANTE: Pedimos permiso permanente para poder volver a ver esta imagen
            // aunque cerremos la app.
            try {
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                getContentResolver().takePersistableUriPermission(imagenUri, takeFlags);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Aquí sacamos el nombre del archivo (ej: "foto.jpg") para mostrarlo en
            // pantalla
            nombreImagen = "Desconocida";
            Cursor cursor = getContentResolver().query(imagenUri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex >= 0) {
                    nombreImagen = cursor.getString(nameIndex);
                }
                cursor.close();
            }

            // Mostramos el nombre de la imagen en el texto de la pantalla
            tvImagenSeleccionada.setText(nombreImagen);
        }
    }

    // Este método recoge toda la información y guarda el producto en la base de
    // datos
    private void guardarProducto() {
        // Obtenemos el texto que escribió el usuario en las cajas
        String nombre = etNombre.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String materiales = etMateriales.getText().toString().trim();

        // Verificamos que no haya dejado nada vacío
        if (nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty() || materiales.isEmpty()
                || nombreImagen == null) {
            Toast.makeText(this, "Completa todos los campos y selecciona una imagen", Toast.LENGTH_SHORT).show();
            return; // Si falta algo, paramos aquí
        }

        double precio;
        int stock;
        try {
            // Convertimos el texto de precio y stock a números
            precio = Double.parseDouble(precioStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Precio o stock inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Guardado en Base de Datos ---
        try {
            // Creamos un nuevo objeto Producto y le ponemos los datos
            Producto nuevoProducto = new Producto();
            nuevoProducto.setNombre(nombre);
            nuevoProducto.setPrecio(precio);
            nuevoProducto.setStock(stock);
            nuevoProducto.setMateriales(materiales);
            // Guardamos la dirección completa de la imagen para encontrarla luego
            nuevoProducto.setImagen(imagenUri.toString());

            // Usamos el DAO (Data Access Object) para insertar el producto en la base de
            // datos
            ProductoDao dao = AppDatabase.getInstance(this).productoDao();
            dao.insert(nuevoProducto);

            Toast.makeText(this, "Producto guardado correctamente", Toast.LENGTH_LONG).show();

            // Limpiamos las cajas de texto para que queden vacías
            etNombre.setText("");
            etPrecio.setText("");
            etMateriales.setText("");
            tvImagenSeleccionada.setText("No se ha seleccionado imagen");
            imagenUri = null;
            nombreImagen = null;

            // Cerramos esta pantalla para volver a la anterior (el inventario)
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

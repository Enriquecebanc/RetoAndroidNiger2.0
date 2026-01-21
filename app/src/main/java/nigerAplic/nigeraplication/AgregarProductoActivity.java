package nigerAplic.nigeraplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import nigerAplic.database.AppDatabase;
import nigerAplic.models.Producto;

public class AgregarProductoActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        db = AppDatabase.getInstance(this);

        EditText etNombre = findViewById(R.id.etNombre);
        EditText etPrecio = findViewById(R.id.etPrecio);
        EditText etMateriales = findViewById(R.id.etMateriales);
        EditText etImagen = findViewById(R.id.etImagen);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(v -> {
            Producto producto = new Producto();
            producto.setNombre(etNombre.getText().toString());
            producto.setPrecio(Double.parseDouble(etPrecio.getText().toString()));
            producto.setMateriales(etMateriales.getText().toString());
            producto.setImagen(etImagen.getText().toString());

            db.productoDao().insert(producto);
            finish();
        });
    }
}

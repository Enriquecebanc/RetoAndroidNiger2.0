package nigerAplic.nigeraplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import nigerAplic.models.Cliente;

/**
 * Actividad con el formulario para añadir nuevos clientes.
 */
public class FormClienteActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etTelefono, etDireccion, etEmail;
    private Button btnAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cliente);

        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etTelefono = findViewById(R.id.etTelefono);
        etDireccion = findViewById(R.id.etDireccion);
        etEmail = findViewById(R.id.etEmail);
        btnAceptar = findViewById(R.id.btnAceptar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCliente();
            }
        });
    }

    /**
     * Recoge los datos de los campos de texto, crea un objeto Cliente y lo guarda
     * en Room.
     */
    private void guardarCliente() {
        String nombre = etNombre.getText().toString();
        String apellido = etApellido.getText().toString();
        String telefono = etTelefono.getText().toString();
        String direccion = etDireccion.getText().toString();
        String email = etEmail.getText().toString();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(this, "Nombre y Apellido son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el objeto Cliente
        Cliente nuevoCliente = new Cliente(nombre, apellido, telefono, direccion, email);

        // Guardar en la Base de Datos mediante Room
        nigerAplic.database.AppDatabase db = nigerAplic.database.AppDatabase.getInstance(this);
        db.clienteDao().insert(nuevoCliente);

        Toast.makeText(this, "Cliente guardado correctamente", Toast.LENGTH_SHORT).show();
        finish(); // Cierra la actividad y vuelve a la anterior
    }
}

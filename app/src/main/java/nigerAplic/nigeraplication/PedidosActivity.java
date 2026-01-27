package nigerAplic.nigeraplication;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import nigerAplic.adapter.PedidosAdapter;
import nigerAplic.database.AppDatabase;
import nigerAplic.models.Pedidin;
import nigerAplic.models.PedidinDao;

public class PedidosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        RecyclerView recyclerPedidos = findViewById(R.id.recyclerPedidos);
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));

        PedidinDao dao = AppDatabase.getInstance(this).pedidinDao();
        List<Pedidin> pedidos = dao.getAll();

        if (pedidos.isEmpty()) {
            Toast.makeText(this, "No hay pedidos registrados", Toast.LENGTH_SHORT).show();
        }

        PedidosAdapter adapter = new PedidosAdapter(pedidos);
        recyclerPedidos.setAdapter(adapter);
    }
}

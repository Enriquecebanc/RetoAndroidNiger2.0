package nigerAplic.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// Interfaz para acceder a los pedidos en la base de datos
@Dao
public interface PedidinDao {
    // Inserta un nuevo pedido en la base de datos
    @Insert
    void insert(Pedidin pedido);

    // Obtiene todos los pedidos ordenados del más reciente al más antiguo
    @Query("SELECT * FROM pedidos ORDER BY id DESC")
    List<Pedidin> getAll();
}

package nigerAplic.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PedidinDao {
    @Insert
    void insert(Pedidin pedido);

    @Query("SELECT * FROM pedidos ORDER BY id DESC")
    List<Pedidin> getAll();
}

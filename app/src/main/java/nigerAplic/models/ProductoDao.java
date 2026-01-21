package nigerAplic.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductoDao {

    @Insert
    void insert(Producto producto);

    @Query("SELECT * FROM productos")
    List<Producto> getAll();

    @Query("DELETE FROM productos")
    void deleteAll();
}

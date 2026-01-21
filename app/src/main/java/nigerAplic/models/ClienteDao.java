package nigerAplic.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ClienteDao {
    @Insert
    void insert(Cliente cliente);

    @Delete
    void delete(Cliente cliente);

    @Query("SELECT * FROM clientes")
    List<Cliente> getAll();
}

package nigerAplic.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// @Dao (Data Access Object) indica que esta interfaz se encarga de las operaciones
// de la base de datos (guardar, leer, borrar...).
@Dao
public interface ProductoDao {

    // Método para guardar un producto nuevo en la base de datos
    @Insert
    void insert(Producto producto);

    // Método para actualizar un producto existente
    @androidx.room.Update
    void update(Producto producto);

    // Método para pedir TODOS los productos que hay en la tabla "productos"
    @Query("SELECT * FROM productos")
    List<Producto> getAll();

    // Método para borrar TODOS los productos (cuidado!)
    @Query("DELETE FROM productos")
    void deleteAll();

    // Método para borrar UN producto específico
    @androidx.room.Delete
    void delete(Producto producto);
}

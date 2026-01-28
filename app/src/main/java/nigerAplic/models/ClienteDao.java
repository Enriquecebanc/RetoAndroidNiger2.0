package nigerAplic.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Interfaz DAO para realizar operaciones sobre la tabla de clientes.
 */
@Dao
public interface ClienteDao {
    /**
     * Inserta un nuevo cliente en la base de datos.
     * 
     * @param cliente Objeto cliente a insertar
     */
    @Insert
    void insert(Cliente cliente);

    /**
     * Elimina un cliente existente de la base de datos.
     * 
     * @param cliente Objeto cliente a eliminar
     */
    @Delete
    void delete(Cliente cliente);

    /**
     * Obtiene todos los clientes de la tabla.
     * 
     * @return Lista de todos los clientes
     */
    @Query("SELECT * FROM clientes")
    List<Cliente> getAll();

    /**
     * Busca un cliente por su nombre completo.
     * 
     * @param fullName Nombre y apellido concatenados
     * @return El cliente encontrado o null si no existe
     */
    @Query("SELECT * FROM clientes WHERE (nombre || ' ' || apellido) = :fullName LIMIT 1")
    Cliente getClienteByFullName(String fullName);
}

package nigerAplic.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import nigerAplic.models.Cliente;
import nigerAplic.models.ClienteDao;
import nigerAplic.models.Producto;
import nigerAplic.models.ProductoDao;
import nigerAplic.models.Pedidin;
import nigerAplic.models.PedidinDao;

// Clase principal de la base de datos de la aplicación
// Define las tablas (entities) y proporciona acceso a los DAOs
@Database(entities = { Cliente.class, Producto.class, Pedidin.class }, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    // Métodos para acceder a cada DAO
    public abstract ClienteDao clienteDao();

    public abstract ProductoDao productoDao();

    public abstract PedidinDao pedidinDao();

    private static volatile AppDatabase INSTANCE;

    // Obtiene la instancia única de la base de datos (patrón Singleton)
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "niger_db")
                            .allowMainThreadQueries() // Permite consultas en el hilo principal (solo para desarrollo)
                            .fallbackToDestructiveMigration() // Borra y recrea la BD si cambia la versión
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

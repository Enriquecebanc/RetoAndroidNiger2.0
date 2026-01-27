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

@Database(entities = { Cliente.class, Producto.class, Pedidin.class }, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ClienteDao clienteDao();

    public abstract ProductoDao productoDao();

    public abstract PedidinDao pedidinDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "niger_db")
                            .allowMainThreadQueries() // SOLO para prácticas
                            .fallbackToDestructiveMigration() // IMPORTANTE al subir versión
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

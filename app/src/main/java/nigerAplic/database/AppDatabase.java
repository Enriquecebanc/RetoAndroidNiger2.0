package nigerAplic.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import nigerAplic.models.Cliente;
import nigerAplic.models.ClienteDao;

@Database(entities = { Cliente.class }, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ClienteDao clienteDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "niger_db")
                            .allowMainThreadQueries() // Simplificación para este reto. En producción no usar esto.
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

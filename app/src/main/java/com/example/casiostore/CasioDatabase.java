package com.example.casiostore;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ProductoEntity.class, CarritoEntity.class}, version = 2, exportSchema = false)
public abstract class CasioDatabase extends RoomDatabase {

    public abstract ProductoDao productoDao();
    public abstract CarritoDao carritoDao();

    private static volatile CasioDatabase INSTANCE;

    public static CasioDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CasioDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CasioDatabase.class, "casio_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
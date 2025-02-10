package com.example.room.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters; // Import necesario
import android.content.Context;

@Database(entities = {Tarea.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class}) // Aquí agrego el convertidor de date
public abstract class TareasDatabase extends RoomDatabase {

    public abstract TareaDAO tareaDAO();

    private static volatile TareasDatabase instancia;

    public static TareasDatabase obtenerInstancia(final Context context) {
        if (instancia == null) {
            synchronized (TareasDatabase.class) {
                if (instancia == null) {
                    instancia = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    TareasDatabase.class,
                                    "base_datos_tareas" //
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instancia;
    }
}

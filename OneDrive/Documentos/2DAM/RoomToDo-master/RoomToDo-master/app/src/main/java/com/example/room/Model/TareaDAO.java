package com.example.room.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TareaDAO {

    @Insert
    void insertar(Tarea tarea);

    @Update
    void actualizar(Tarea tarea);

    @Delete
    void eliminar(Tarea tarea);

    @Query("SELECT * FROM tabla_tareas ORDER BY fecha ASC")
    LiveData<List<Tarea>> obtenerTodasLasTareas();

    @Query("SELECT * FROM tabla_tareas WHERE nombre = :nombre")
    Tarea obtenerTareaPorNombre(String nombre);

    @Query("DELETE FROM tabla_tareas")
    void eliminarTodasLasTareas();

    @Query("SELECT * FROM tabla_tareas WHERE completada = 0")
    LiveData<List<Tarea>> obtenerTareasNoCompletadas();

    @Query("SELECT * FROM tabla_tareas WHERE nombre LIKE :nombre")
    LiveData<List<Tarea>> obtenerTareasPorNombre(String nombre);

}


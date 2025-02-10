package com.example.room.UI;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.room.Model.Tarea;
import com.example.room.Model.TareasRepositorio;

import java.util.Date;
import java.util.List;

public class TareasViewModel extends AndroidViewModel {

    private final TareasRepositorio repositorio;
    private final LiveData<List<Tarea>> todasLasTareas;

    // Constructor del ViewModel.
    public TareasViewModel(Application aplicacion) {
        super(aplicacion);
        repositorio = new TareasRepositorio(aplicacion);
        todasLasTareas = repositorio.obtenerTodasLasTareas();
        //Solo inicializar una vez---
        //inicializarTareasDeMuestra();
    }

    private void inicializarTareasDeMuestra() {
        repositorio.eliminarTodasLasTareas();
        repositorio.insertar(new Tarea("Estudiar programación", "Repasar conceptos de Java y Android",
                new Date(), null));
        repositorio.insertar(new Tarea("Hacer ejercicio", "Correr 30 minutos por la mañana",
                new Date(), null));
        repositorio.insertar(new Tarea("Reunión con el equipo", "Revisar avances de proyectos",
                new Date(), null));
        repositorio.insertar(new Tarea("Llamar a la familia", "Hablar con mis padres y hermanos",
                new Date(), null));
        repositorio.insertar(new Tarea("Comprar comida", "Ir al supermercado a comprar ingredientes",
                new Date(), null));
        repositorio.insertar(new Tarea("Preparar presentación", "Crear diapositivas para la reunión",
                new Date(), null));
        repositorio.insertar(new Tarea("Revisar correo", "Leer los correos importantes",
                new Date(), null));
        repositorio.insertar(new Tarea("Lavar la ropa", "Poner la lavadora y colgar la ropa",
                new Date(), null));
        repositorio.insertar(new Tarea("Ver una película", "Mirar una película de acción para descansar",
                new Date(), null));
        repositorio.insertar(new Tarea("Leer libro", "Leer 30 páginas del libro actual",
                new Date(), null));
    }

    public void insertar(Tarea tarea) {
        repositorio.insertar(tarea);
    }

    public void actualizar(Tarea tarea) {
        repositorio.actualizar(tarea);
    }

    public void eliminar(Tarea tarea) {
        repositorio.eliminar(tarea);
    }

    public void eliminarTodasLasTareas() {
        repositorio.eliminarTodasLasTareas();
    }

    public LiveData<List<Tarea>> obtenerTodasLasTareas(){
        return repositorio.obtenerTodasLasTareas();
    }

    public LiveData<List<Tarea>> obtenerTareasNoCompletadas() {
        return repositorio.obtenerTareasNoCompletadas();
    }
    public LiveData<List<Tarea>> obtenerTareasFiltradasPorNombre(String nombre) {
        return repositorio.obtenerTareasFiltradasPorNombre(nombre);
    }

}

package com.example.room.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "tabla_tareas")
public class Tarea implements java.io.Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nombre;
    private String descripcion;
    private Date fecha;
    private boolean completada;
    private byte[] imagen;



    public Tarea(String nombre, String descripcion, Date fecha, byte[] imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.completada = false;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}

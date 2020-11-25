package com.ovalle.to_do.entidades;

public class Tarea {
    private String id;
    private String nombre;
    private String descripcion;
    private String tarea;

    public Tarea() {
    }

    public Tarea(String id, String nombre, String descripcion, String tarea) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tarea = tarea;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }
}

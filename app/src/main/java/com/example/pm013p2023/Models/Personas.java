package com.example.pm013p2023.Models;

public class Personas
{
    private Integer id;
    private String nombre;
    private Integer telefono;
    private String nota;

    public Personas(Integer id, String nombre, Integer telefono, String nota) {
        this.id = id;
        this.nombre = nombre;
        telefono = telefono;
        nota = nota;
    }

    public Personas() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer edad) {
        telefono = telefono;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) { nota = nota;}
}

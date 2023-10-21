package com.example.pm013p2023.configuracion;

import java.lang.reflect.Array;

public class Transacciones {
    // Nombre de la base de datos
    public static final String namedb = "DB_contacts";

    //Tablas de la base de datos
    public static final String Tabla = "Contactos";

    // Campos de la tabla
    public static final String id = "id";
    public static final String nombre = "nombre";
    public static final String telefono = "telefono";
    public static final String nota = "nota";

    public static final String IMAGE = "imagen";


    // Consultas de Base de datos
    //ddl
    public static final String CreateTableContactos =
            "CREATE TABLE Contactos (\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    nombre TEXT,\n" +
            "    telefono INTEGER,\n" +
            "    nota TEXT\n" +
            ");";


    public static final String DropTableContactos = "DROP TABLE IF EXISTS Contactos";

    //dml
    public static final String SelectTableContactos = "SELECT * FROM " + Transacciones.Tabla;

}

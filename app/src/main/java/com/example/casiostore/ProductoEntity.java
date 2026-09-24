package com.example.casiostore;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "productos")
public class ProductoEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public double precio;
    public String descripcion;
    public int imagenRes;
    public String categoria;
    public int cantidad;

    // Constructor vacío requerido por Room
    public ProductoEntity() {
    }

    // Constructor con los 6 parámetros que estás usando en el CasioDatabase
    public ProductoEntity(String nombre, double precio, String descripcion, int imagenRes, String categoria, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagenRes = imagenRes;
        this.categoria = categoria;
        this.cantidad = cantidad;
    }
}
package com.example.casiostore;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "productos")
public class ProductoEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public String descripcion;
    public double precio;
    public String imagenRes;
    public String categoria;

    // Constructor vacío requerido por Room (opcional pero recomendado si usas consultas específicas)
    public ProductoEntity() {
    }

    // Constructor completo con los 5 parámetros
    public ProductoEntity(String nombre, String descripcion, double precio, String imagenRes, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenRes = imagenRes;
        this.categoria = categoria;
    }
}
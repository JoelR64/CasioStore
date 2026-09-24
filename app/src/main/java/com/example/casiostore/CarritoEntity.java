package com.example.casiostore;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "carrito")
public class CarritoEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public double precio;
    public int cantidad;
    public int imagenRes; // <-- CAMBIADO A INT
    public String descripcion;

    public CarritoEntity(String nombre, double precio, int cantidad, int imagenRes, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.imagenRes = imagenRes;
        this.descripcion = descripcion;
    }
}
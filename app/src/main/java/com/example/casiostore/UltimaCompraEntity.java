package com.example.casiostore;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ultima_compra")
public class UltimaCompraEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public double precio;
    public int cantidad;
    public int imagenRes;
    public String descripcion;

    // Constructor vacío requerido por Room
    public UltimaCompraEntity() {
    }

    public UltimaCompraEntity(String nombre, double precio, int cantidad, int imagenRes, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.imagenRes = imagenRes;
        this.descripcion = descripcion;
    }
}
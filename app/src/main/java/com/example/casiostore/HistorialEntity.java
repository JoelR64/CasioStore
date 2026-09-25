package com.example.casiostore;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "historial_compras")
public class HistorialEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombreProducto;
    public double monto;
    public String fecha;

    public HistorialEntity(String nombreProducto, double monto, String fecha) {
        this.nombreProducto = nombreProducto;
        this.monto = monto;
        this.fecha = fecha;
    }
}
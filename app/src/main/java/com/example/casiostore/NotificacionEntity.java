package com.example.casiostore;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notificaciones")
public class NotificacionEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String fecha;
    public String mensaje;

    public NotificacionEntity() {
    }

    public NotificacionEntity(String fecha, String mensaje) {
        this.fecha = fecha;
        this.mensaje = mensaje;
    }
}
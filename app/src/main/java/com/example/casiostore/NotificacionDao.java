package com.example.casiostore;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface NotificacionDao {
    @Insert
    void insertar(NotificacionEntity notificacion);

    @Query("SELECT * FROM notificaciones ORDER BY id DESC")
    List<NotificacionEntity> obtenerNotificaciones();
}
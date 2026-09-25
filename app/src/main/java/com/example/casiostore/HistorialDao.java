package com.example.casiostore;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface HistorialDao {
    @Insert
    void insertarCompra(HistorialEntity compra);

    @Query("SELECT * FROM historial_compras ORDER BY id DESC")
    List<HistorialEntity> obtenerHistorial();
}
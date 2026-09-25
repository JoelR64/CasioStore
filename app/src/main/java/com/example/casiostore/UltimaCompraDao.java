package com.example.casiostore;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface UltimaCompraDao {
    @Insert
    void insertar(UltimaCompraEntity item);

    @Query("DELETE FROM ultima_compra")
    void vaciarUltimaCompra();

    @Query("SELECT * FROM ultima_compra")
    List<UltimaCompraEntity> obtenerUltimaCompra();
}
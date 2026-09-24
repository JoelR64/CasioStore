package com.example.casiostore;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface CarritoDao {

    @Query("SELECT * FROM carrito")
    List<CarritoEntity> obtenerCarrito();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarAlCarrito(CarritoEntity item);

    @Query("DELETE FROM carrito")
    void vaciarCarrito();
}
package com.example.casiostore;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(ProductoEntity producto); // <-- El método se llama "insertar"

    @Query("SELECT * FROM productos")
    List<ProductoEntity> obtenerTodos();

    @Query("SELECT * FROM productos WHERE categoria = :cat")
    List<ProductoEntity> obtenerPorCategoria(String cat);
}
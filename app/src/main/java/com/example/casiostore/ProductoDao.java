package com.example.casiostore;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ProductoDao {
    @Query("SELECT * FROM productos")
    List<ProductoEntity> obtenerTodos();

    @Query("SELECT * FROM productos WHERE categoria = :categoriaFiltro COLLATE NOCASE")
    List<ProductoEntity> obtenerPorCategoria(String categoriaFiltro);

    @Insert
    void insertarProducto(ProductoEntity producto);
}
package com.example.casiostore;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(UsuarioEntity usuario); // <-- Este método debe existir

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    UsuarioEntity iniciarSesion(String email, String password);
}
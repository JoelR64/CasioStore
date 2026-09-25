package com.example.casiostore;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(UsuarioEntity usuario);

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    UsuarioEntity iniciarSesion(String email, String password);

    // --- NUEVOS MÉTODOS PARA LA TARJETA ---
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    UsuarioEntity obtenerUsuarioPorCorreo(String email);

    @Update
    void actualizar(UsuarioEntity usuario);
}
package com.example.casiostore;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class UsuarioEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String email;
    public String password;
    public String nombre;

    // Constructor vacío
    public UsuarioEntity() {
    }

    // Constructor con parámetros para facilitar el registro/inserción
    public UsuarioEntity(String email, String password, String nombre) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
    }
}
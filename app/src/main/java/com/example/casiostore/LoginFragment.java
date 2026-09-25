package com.example.casiostore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.concurrent.Executors;

public class LoginFragment extends Fragment {

    private EditText inputCorreo, inputPassword;
    private Button btnConfirmar;

    public LoginFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_login, container, false);

        inputCorreo = vista.findViewById(R.id.inputCorreo);
        inputPassword = vista.findViewById(R.id.inputPassword);
        btnConfirmar = vista.findViewById(R.id.btnConfirmarLogin);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = inputCorreo.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (correo.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getContext(), "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Consultar en la base de datos Room usando el hilo secundario
                Executors.newSingleThreadExecutor().execute(() -> {
                    CasioDatabase db = CasioDatabase.getDatabase(getContext());
                    UsuarioEntity usuarioEncontrado = db.usuarioDao().iniciarSesion(correo, password);

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (usuarioEncontrado != null) {
                                // ¡Credenciales correctas! Pasamos a la PANTALLA2 enviando el nombre del usuario
                                Toast.makeText(getContext(), "¡Bienvenido " + usuarioEncontrado.nombre + "!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getActivity(), pantallaDos.class);
                                intent.putExtra("Usuario", usuarioEncontrado.nombre);
                                startActivity(intent);

                                // Opcional: finalizar MainActivity para que no vuelva atrás con el botón de retroceso
                                getActivity().finish();
                            } else {
                                // Credenciales incorrectas
                                Toast.makeText(getContext(), "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        return vista;
    }
}
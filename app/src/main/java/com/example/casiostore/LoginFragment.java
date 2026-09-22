package com.example.casiostore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_login, container, false);

        EditText inputCorreo = vista.findViewById(R.id.inputCorreo);
        Button btnConfirmar = vista.findViewById(R.id.btnConfirmarLogin);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = inputCorreo.getText().toString();

                // Pasa a la PANTALLA2 enviando el correo como extra si lo deseas
                Intent intent = new Intent(getActivity(), pantallaDos.class);
                intent.putExtra("Usuario", correo.isEmpty() ? "Usuario" : correo);
                startActivity(intent);
            }
        });

        return vista;
    }
}
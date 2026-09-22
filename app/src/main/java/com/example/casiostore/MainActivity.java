package com.example.casiostore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnIrLogin = findViewById(R.id.btnIrLogin);
        Button btnInvitado = findViewById(R.id.btnInvitado);

        // 1. Botón "Iniciar Sesión": Carga el fragmento de correo y contraseña en el contenedor
        btnIrLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorLogin, loginFragment);
                transaction.commit();
                // Ocultamos los botones de la pantalla principal para que no estorben
                btnIrLogin.setVisibility(View.GONE);
                btnInvitado.setVisibility(View.GONE);
            }
        });

        // 2. Botón "Continuar como invitado": Salta directo a la PANTALLA2
        btnInvitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, pantallaDos.class);
                intent.putExtra("Usuario", "Invitado"); // Opcional: le manda el nombre "Invitado"
                startActivity(intent);
            }
        });
    }


}
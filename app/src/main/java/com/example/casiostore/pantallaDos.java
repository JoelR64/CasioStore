package com.example.casiostore;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class pantallaDos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_dos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencias a los elementos de la barra superior
        TextView textoUno = findViewById(R.id.textoUno);
        ImageView btnAtrasGeneral = findViewById(R.id.btnAtrasGeneral);

        // Referencias a los botones del menú inferior
        LinearLayout btnInicio = findViewById(R.id.botonInicio);
        LinearLayout btnCarrito = findViewById(R.id.botonCarrito);
        LinearLayout btnUsuario = findViewById(R.id.botonUsuario);

        // 1. Cargar por defecto el fragmento de Inicio al abrir la pantalla
        if (savedInstanceState == null) {
            cargarFragmento(new InicioFragment());
            textoUno.setText("Casio Store |");
            btnAtrasGeneral.setVisibility(View.GONE); // Oculto en Inicio
        }

        // 2. Programar el clic para el botón Inicio
        btnInicio.setOnClickListener(v -> {
            cargarFragmento(new InicioFragment());
            textoUno.setText("Casio Store |");
            btnAtrasGeneral.setVisibility(View.GONE); // Se oculta en Inicio
        });

        // 3. Programar el clic para el botón Carrito
        btnCarrito.setOnClickListener(v -> {
            cargarFragmento(new CarritoFragment());
            textoUno.setText("TU CARRITO");
            btnAtrasGeneral.setVisibility(View.VISIBLE); // Aparece en Carrito
        });

        // 4. Programar el clic para el botón Usuario
        btnUsuario.setOnClickListener(v -> {
            cargarFragmento(new UsuarioFragment());
            textoUno.setText("MI PERFIL");
            btnAtrasGeneral.setVisibility(View.VISIBLE); // Aparece en Usuario
        });

        // 5. Programar la acción del botón de retroceso general
        btnAtrasGeneral.setOnClickListener(v -> {
            // Al presionarlo, regresa a la pantalla de Inicio
            cargarFragmento(new InicioFragment());
            textoUno.setText("Casio Store |");
            btnAtrasGeneral.setVisibility(View.GONE);
        });
    }

    // Método auxiliar reutilizable para reemplazar los fragmentos en el contenedor
    private void cargarFragmento(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedorprincipal, fragment);
        transaction.commit();
    }
}
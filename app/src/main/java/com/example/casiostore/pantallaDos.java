package com.example.casiostore;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

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

        // 1. Cargar por defecto el fragmento de Inicio al abrir la pantalla
        if (savedInstanceState == null) {
            cargarFragmento(new InicioFragment()); // Cambia por el nombre real de tu fragmento de inicio
        }
        // 2. Referenciar los LinearLayouts de la barra inferior de tu XML
        LinearLayout btnInicio = findViewById(R.id.botonInicio);
        LinearLayout btnCarrito = findViewById(R.id.botonCarrito);
        LinearLayout btnUsuario = findViewById(R.id.botonUsuario);

        // 3. Programar el clic para el botón Inicio
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFragmento(new InicioFragment()); // Tu fragmento de inicio
            }
        });

        // 4. Programar el clic para el botón Carrito
        btnCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFragmento(new CarritoFragment()); // Tu fragmento de carrito
            }
        });

        // 5. Programar el clic para el botón Usuario
        btnUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFragmento(new UsuarioFragment()); // Tu fragmento de usuario
            }
        });
        // --- FIN DE LA LÓGICA DE FRAGMENTOS ---
    }

    // Método auxiliar reutilizable para reemplazar los fragmentos en el contenedor
    private void cargarFragmento(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedorprincipal, fragment);
        transaction.commit();
    }
}
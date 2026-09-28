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

    // Variables para el contador del easter egg (pantalla ghost)
    private int userClickCount = 0;
    private long lastClickTime = 0;

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
        ImageView btnCampanaNotificaciones = findViewById(R.id.btnCampanaNotificaciones);

        // Referencias a los botones del menú inferior
        LinearLayout btnInicio = findViewById(R.id.botonInicio);
        LinearLayout btnCarrito = findViewById(R.id.botonCarrito);
        LinearLayout btnUsuario = findViewById(R.id.botonUsuario);

        // 1. Cargar por defecto el fragmento de Inicio al abrir la pantalla
        if (savedInstanceState == null) {
            cargarFragmento(new InicioFragment());
            textoUno.setText("Casio Store |");
            btnAtrasGeneral.setVisibility(View.GONE);
        }

        // 2. Programar el clic para el botón Inicio
        btnInicio.setOnClickListener(v -> {
            cargarFragmento(new InicioFragment());
            textoUno.setText("Casio Store |");
            btnAtrasGeneral.setVisibility(View.GONE);
            userClickCount = 0; // Reiniciar contador si navega a otro lado
        });

        // 3. Programar el clic para el botón Carrito
        btnCarrito.setOnClickListener(v -> {
            cargarFragmento(new CarritoFragment());
            textoUno.setText("TU CARRITO");
            btnAtrasGeneral.setVisibility(View.VISIBLE);
            userClickCount = 0; // Reiniciar contador
        });

        // 4. LÓGICA DE LA PANTALLA GHOST EN EL BOTÓN USUARIO (5 TOQUES RÁPIDOS)
        btnUsuario.setOnClickListener(v -> {
            long currentTime = System.currentTimeMillis();

            // Si el tiempo entre toque y toque es menor a 800 milisegundos, cuenta como consecutivo
            if (currentTime - lastClickTime < 800) {
                userClickCount++;
            } else {
                userClickCount = 1; // Se reinicia si pasa mucho tiempo
            }
            lastClickTime = currentTime;

            // Si llega a los 5 toques rápidos, activa la pantalla ghost
            if (userClickCount >= 5) {
                userClickCount = 0; // Reseteamos el contador
                cargarFragmento(new GhostFragment());
                textoUno.setText("PANTALLA FANTASMA");
                btnAtrasGeneral.setVisibility(View.VISIBLE);
            } else {
                // Comportamiento normal: abre el perfil de usuario habitual
                // NOTA: Si aquí te manda al login, asegúrate de que UsuarioFragment
                // evalúe si hay sesión activa antes de redirigir.
                cargarFragmento(new UsuarioFragment());
                textoUno.setText("MI PERFIL");
                btnAtrasGeneral.setVisibility(View.VISIBLE);
            }
        });

        // 5. Programar el clic para el botón de la campana de notificaciones
        if (btnCampanaNotificaciones != null) {
            btnCampanaNotificaciones.setOnClickListener(v -> {
                cargarFragmento(new NotificacionesFragment());
                textoUno.setText("NOTIFICACIONES");
                btnAtrasGeneral.setVisibility(View.VISIBLE);
                userClickCount = 0;
            });
        }

        // 6. Programar la acción del botón de retroceso general
        btnAtrasGeneral.setOnClickListener(v -> {
            cargarFragmento(new InicioFragment());
            textoUno.setText("Casio Store |");
            btnAtrasGeneral.setVisibility(View.GONE);
            userClickCount = 0;
        });
    }

    // Método auxiliar reutilizable para reemplazar los fragmentos en el contenedor
    private void cargarFragmento(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedorprincipal, fragment);
        transaction.commit();
    }
}
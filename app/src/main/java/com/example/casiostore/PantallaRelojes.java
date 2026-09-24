package com.example.casiostore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executors;

public class PantallaRelojes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_relojes);

        // Referencias de la barra superior
        ImageView btnAtras = findViewById(R.id.btnAtras);
        TextView txtTituloSeccion = findViewById(R.id.textoDos);

        // 1. Recibir la categoría enviada desde el Intent
        String categoriaSeleccionada = getIntent().getStringExtra("CATEGORIA");

        // 2. Cambiar el texto superior dinámicamente según la categoría
        if (categoriaSeleccionada != null) {
            txtTituloSeccion.setText(categoriaSeleccionada);
        } else {
            txtTituloSeccion.setText("Productos");
        }

        // Botón para regresar
        if (btnAtras != null) {
            btnAtras.setOnClickListener(v -> finish());
        }

        // Configurar el RecyclerView en PantallaRelojes para mostrar solo los de esa categoría
        RecyclerView recyclerView = findViewById(R.id.recyclerProductosPantalla);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        Executors.newSingleThreadExecutor().execute(() -> {
            CasioDatabase db = CasioDatabase.getDatabase(this);
            List<ProductoEntity> listaFiltrada = db.productoDao().obtenerPorCategoria(categoriaSeleccionada);

            runOnUiThread(() -> {
                ProductoAdapter adapter = new ProductoAdapter(listaFiltrada, producto -> {
                    // Lógica al hacer clic en un producto filtrado (opcional: abrir detalle)
                    Intent intent = new Intent(PantallaRelojes.this, DetalleProductoActivity.class);
                    intent.putExtra("NOMBRE", producto.nombre);
                    intent.putExtra("PRECIO", producto.precio);
                    intent.putExtra("DESCRIPCION", producto.descripcion);
                    intent.putExtra("IMAGEN", producto.imagenRes);
                    startActivity(intent);
                });
                recyclerView.setAdapter(adapter);
            });
        });
    }

    private void seleccionarFragmentoSegunCategoria(String categoria) {
        switch (categoria) {
            case "CALCULADORAS":
                cargarFragmento(new CalculadorasFragment());
                break;
            case "TECLADOS":
                cargarFragmento(new TecladosFragment());
                break;
            case "RELOJES":
            default:
                cargarFragmento(new RelojesFragment());
                break;
        }
    }

    private void cargarFragmento(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedorprincipal, fragment);
        transaction.commit();
    }
}
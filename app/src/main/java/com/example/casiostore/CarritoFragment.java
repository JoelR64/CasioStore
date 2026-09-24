package com.example.casiostore;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;
import java.util.concurrent.Executors;

public class CarritoFragment extends Fragment {

    private RecyclerView recyclerCarrito;
    private LinearLayout layoutVacio;
    private LinearLayout layoutContenidoCarrito; // Contenedor de la tabla y botón pagar
    private ImageView btnSumarInicio; // El botón "+" para ir al inicio

    public CarritoFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del fragmento de carrito
        return inflater.inflate(R.layout.fragment_carrito, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Referencias de los elementos en el XML del fragmento
        recyclerCarrito = view.findViewById(R.id.recyclerCarrito);
        layoutVacio = view.findViewById(R.id.layoutVacio);
        layoutContenidoCarrito = view.findViewById(R.id.layoutContenidoCarrito);
        btnSumarInicio = view.findViewById(R.id.btnSumarInicio);

        recyclerCarrito.setLayoutManager(new LinearLayoutManager(getContext()));

        // Acción del botón "+" cuando el carrito está vacío: te lleva al inicio de forma segura
        if (btnSumarInicio != null) {
            btnSumarInicio.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contenedorprincipal, new InicioFragment())
                            .commit();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cada vez que el usuario entre a la pestaña del carrito, consultamos la base de datos
        cargarDatosCarrito();
    }

    private void cargarDatosCarrito() {
        Executors.newSingleThreadExecutor().execute(() -> {
            CasioDatabase db = CasioDatabase.getDatabase(getContext());
            List<CarritoEntity> listaCarrito = db.carritoDao().obtenerCarrito();

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (listaCarrito == null || listaCarrito.isEmpty()) {
                        // Mostrar estado vacío y ocultar contenido
                        if (layoutVacio != null) layoutVacio.setVisibility(View.VISIBLE);
                        if (layoutContenidoCarrito != null) layoutContenidoCarrito.setVisibility(View.GONE);
                    } else {
                        // Ocultar estado vacío y mostrar la tabla con productos
                        if (layoutVacio != null) layoutVacio.setVisibility(View.GONE);
                        if (layoutContenidoCarrito != null) layoutContenidoCarrito.setVisibility(View.VISIBLE);

                        if (recyclerCarrito != null) {
                            CarritoAdapter adapter = new CarritoAdapter(listaCarrito);
                            recyclerCarrito.setAdapter(adapter);
                        }
                    }
                });
            }
        });
    }
}
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class CarritoFragment extends Fragment {

    private RecyclerView recyclerCarrito;
    private LinearLayout layoutVacio;
    private LinearLayout layoutContenidoCarrito;
    private ImageView btnSumarInicio;
    private TextView txtSubtotalGeneral; // Referencia al texto del subtotal global

    public CarritoFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carrito, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerCarrito = view.findViewById(R.id.recyclerCarrito);
        layoutVacio = view.findViewById(R.id.layoutVacio);
        layoutContenidoCarrito = view.findViewById(R.id.layoutContenidoCarrito);
        btnSumarInicio = view.findViewById(R.id.btnSumarInicio);
        txtSubtotalGeneral = view.findViewById(R.id.txtSubtotalGeneral); // Enlazar el TextView

        recyclerCarrito.setLayoutManager(new LinearLayoutManager(getContext()));

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
        cargarDatosCarrito();
    }

    private void cargarDatosCarrito() {
        Executors.newSingleThreadExecutor().execute(() -> {
            CasioDatabase db = CasioDatabase.getDatabase(getContext());
            List<CarritoEntity> listaOriginal = db.carritoDao().obtenerCarrito();

            // Lógica para agrupar elementos repetidos por nombre y sumar cantidades
            Map<String, CarritoEntity> mapaAgrupado = new LinkedHashMap<>();
            double sumaTotalGeneral = 0.0;

            if (listaOriginal != null) {
                for (CarritoEntity item : listaOriginal) {
                    if (mapaAgrupado.containsKey(item.nombre)) {
                        // Si ya existe, sumamos la cantidad
                        CarritoEntity existente = mapaAgrupado.get(item.nombre);
                        existente.cantidad += item.cantidad;
                    } else {
                        // Si es nuevo en el mapa, lo agregamos
                        mapaAgrupado.put(item.nombre, item);
                    }
                }
            }

            List<CarritoEntity> listaAgrupada = new ArrayList<>(mapaAgrupado.values());

            // Calcular la sumatoria de los subtotales de cada producto agrupado
            for (CarritoEntity item : listaAgrupada) {
                sumaTotalGeneral += (item.precio * item.cantidad);
            }

            final double totalFinal = sumaTotalGeneral;

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (listaAgrupada.isEmpty()) {
                        if (layoutVacio != null) layoutVacio.setVisibility(View.VISIBLE);
                        if (layoutContenidoCarrito != null) layoutContenidoCarrito.setVisibility(View.GONE);
                    } else {
                        if (layoutVacio != null) layoutVacio.setVisibility(View.GONE);
                        if (layoutContenidoCarrito != null) layoutContenidoCarrito.setVisibility(View.VISIBLE);

                        // Actualizar el texto del subtotal general en negrita
                        if (txtSubtotalGeneral != null) {
                            txtSubtotalGeneral.setText("Subtotal: Bs. " + totalFinal);
                        }

                        if (recyclerCarrito != null) {
                            CarritoAdapter adapter = new CarritoAdapter(listaAgrupada);
                            recyclerCarrito.setAdapter(adapter);
                        }
                    }
                });
            }
        });
    }
}
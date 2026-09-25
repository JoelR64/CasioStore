package com.example.casiostore;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button btnPagar;
    private TextView txtSubtotalGeneral;

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
        btnPagar = view.findViewById(R.id.btnPagar);
        txtSubtotalGeneral = view.findViewById(R.id.txtSubtotalGeneral);

        recyclerCarrito.setLayoutManager(new LinearLayoutManager(getContext()));

        // Botón para ir al inicio si el carrito está vacío
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

        // Botón PAGAR con validación de invitado
        if (btnPagar != null) {
            btnPagar.setOnClickListener(v -> {
                if (getActivity() != null && getActivity().getIntent() != null) {
                    String usuarioActual = getActivity().getIntent().getStringExtra("Usuario");

                    // Validar si entró como invitado
                    if ("Invitado".equals(usuarioActual) || usuarioActual == null) {
                        Toast.makeText(getContext(), "Debes iniciar sesión para realizar una compra", Toast.LENGTH_LONG).show();

                        // Redirigir al login principal (MainActivity)
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        return;
                    }
                }

                // Si es un usuario registrado, avanza al flujo de pago normal
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contenedorprincipal, new PagoFragment())
                            .addToBackStack(null)
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

            Map<String, CarritoEntity> mapaAgrupado = new LinkedHashMap<>();
            double sumaTotalGeneral = 0.0;

            if (listaOriginal != null) {
                for (CarritoEntity item : listaOriginal) {
                    if (mapaAgrupado.containsKey(item.nombre)) {
                        CarritoEntity existente = mapaAgrupado.get(item.nombre);
                        existente.cantidad += item.cantidad;
                    } else {
                        mapaAgrupado.put(item.nombre, item);
                    }
                }
            }

            List<CarritoEntity> listaAgrupada = new ArrayList<>(mapaAgrupado.values());

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
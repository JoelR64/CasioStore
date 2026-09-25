package com.example.casiostore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DetalleCompraFragment extends Fragment {

    private RecyclerView rvDetalleProductos;
    private TextView tvSubtotalDetalle;
    private Button btnHacerOtraCompra;

    public DetalleCompraFragment() {
        // Constructor público requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_compra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvDetalleProductos = view.findViewById(R.id.rvDetalleProductos);
        tvSubtotalDetalle = view.findViewById(R.id.tvSubtotalDetalle);
        btnHacerOtraCompra = view.findViewById(R.id.btnHacerOtraCompra);

        if (rvDetalleProductos != null) {
            rvDetalleProductos.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        // Cargar los productos de la última compra realizada
        cargarDetalleCompra();

        if (btnHacerOtraCompra != null) {
            btnHacerOtraCompra.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contenedorprincipal, new InicioFragment())
                            .commit();
                }
            });
        }
    }

    private void cargarDetalleCompra() {
        Executors.newSingleThreadExecutor().execute(() -> {
            CasioDatabase db = CasioDatabase.getDatabase(getContext());

            // Consultamos únicamente la tabla temporal de la última compra
            List<UltimaCompraEntity> listaUltima = db.ultimaCompraDao().obtenerUltimaCompra();

            double sumaTotal = 0.0;
            List<CarritoEntity> listaParaAdapter = new ArrayList<>();

            if (listaUltima != null) {
                for (UltimaCompraEntity u : listaUltima) {
                    double totalItem = u.precio * u.cantidad;
                    sumaTotal += totalItem;

                    // Mapeamos a CarritoEntity para reutilizar el CarritoAdapter existente
                    CarritoEntity itemTemp = new CarritoEntity();
                    itemTemp.nombre = u.nombre;
                    itemTemp.precio = u.precio;
                    itemTemp.cantidad = u.cantidad;
                    itemTemp.imagenRes = u.imagenRes;
                    itemTemp.descripcion = u.descripcion;

                    listaParaAdapter.add(itemTemp);
                }
            }

            final double totalFinal = sumaTotal;

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (tvSubtotalDetalle != null) {
                        tvSubtotalDetalle.setText("SUBTOTAL  Bs. " + totalFinal);
                    }

                    if (rvDetalleProductos != null && !listaParaAdapter.isEmpty()) {
                        CarritoAdapter adapter = new CarritoAdapter(listaParaAdapter);
                        rvDetalleProductos.setAdapter(adapter);
                    }
                });
            }
        });
    }
}
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

import java.util.List;
import java.util.concurrent.Executors;

public class DetalleCompraFragment extends Fragment {

    private RecyclerView rvDetalleProductos;
    private TextView tvSubtotalDetalle;
    private Button btnHacerOtraCompra;

    public DetalleCompraFragment() {
        // Constructor público
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

        cargarDetalleCompra();

        if (btnHacerOtraCompra != null) {
            btnHacerOtraCompra.setOnClickListener(v -> {
                Executors.newSingleThreadExecutor().execute(() -> {
                    CasioDatabase db = CasioDatabase.getDatabase(getContext());
                    db.carritoDao().vaciarCarrito();

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.contenedorprincipal, new InicioFragment())
                                    .commit();
                        });
                    }
                });
            });
        }
    }

    private void cargarDetalleCompra() {
        Executors.newSingleThreadExecutor().execute(() -> {
            CasioDatabase db = CasioDatabase.getDatabase(getContext());
            List<CarritoEntity> lista = db.carritoDao().obtenerCarrito();

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (tvSubtotalDetalle != null) {
                        tvSubtotalDetalle.setText("SUBTOTAL  Bs. 11,050");
                    }

                    if (rvDetalleProductos != null && lista != null && !lista.isEmpty()) {
                        // Usamos CarritoAdapter que ya está definido en tu proyecto
                        rvDetalleProductos.setAdapter(new CarritoAdapter(lista));
                    }
                });
            }
        });
    }
}